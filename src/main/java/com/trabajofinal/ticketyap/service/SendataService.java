package com.trabajofinal.ticketyap.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.trabajofinal.ticketyap.dao.EntradaDao;
import com.trabajofinal.ticketyap.dao.UserDao;
import com.trabajofinal.ticketyap.exceptions.PdfValidacionException;
import com.trabajofinal.ticketyap.modelos.Author;
import com.trabajofinal.ticketyap.modelos.Entradatmp;
import com.trabajofinal.ticketyap.modelos.DTO.ConfirmacionPdfData;
import com.trabajofinal.ticketyap.modelos.DTO.FileResponse;

@Service
public class SendataService {

    private static final Logger log = LoggerFactory.getLogger(SendataService.class);
    private static final int DPI_RENDER = 150;
    private static final long MAX_SIZE_BYTES = 10 * 1024 * 1024L;

    private final MinioService minioService;
    private final EntradatmpService entradatmpService;
    private final BarcodeRegistroService barcodeRegistroService;
    private final UserDao userDao;
    private final EntradaDao entradaDao;

    public SendataService(MinioService minioService,
                          EntradatmpService entradatmpService,
                          BarcodeRegistroService barcodeRegistroService,
                          UserDao userDao,
                          EntradaDao entradaDao) {
        this.minioService           = minioService;
        this.entradatmpService      = entradatmpService;
        this.barcodeRegistroService = barcodeRegistroService;
        this.userDao                = userDao;
        this.entradaDao             = entradaDao;
    }

    
    @Transactional
    public FileResponse procesarPdf(MultipartFile archivo) {
        log.info("[Sendata] ── INICIO procesarPdf ──────────────────────────────");
        log.info("[Sendata] Archivo recibido: nombre='{}', tamaño={} bytes, contentType='{}'",
                 archivo.getOriginalFilename(), archivo.getSize(), archivo.getContentType());

        validarArchivo(archivo);
        log.info("[Sendata] [1/8] Validación de archivo OK");

        Integer idUsuario = idUsuarioActual();
        log.info("[Sendata] [1/8] Usuario autenticado: id={}", idUsuario);

        byte[] bytes;
        try {
            bytes = archivo.getBytes();
            log.debug("[Sendata] Bytes leídos del archivo: {} bytes", bytes.length);
        } catch (IOException e) {
            log.error("[Sendata] Error al leer bytes del archivo: {}", e.getMessage(), e);
            throw new PdfValidacionException("No se pudo leer el archivo: " + e.getMessage());
        }

        // 2. Calcular hash del PDF
        String hash;
        try {
            hash = minioService.calcularHashPdf(new ByteArrayInputStream(bytes));
            log.info("[Sendata] [2/8] Hash SHA-256 calculado: {}", hash);
        } catch (Exception e) {
            log.error("[Sendata] Error al calcular hash: {}", e.getMessage(), e);
            throw new PdfValidacionException("No se pudo calcular el hash del PDF: " + e.getMessage());
        }

        // 3. Renderizar páginas, extraer barcodes y metadatos del PDF
        List<String> codigos  = new ArrayList<>();
        List<byte[]> imagenes = new ArrayList<>();
        int numeroPaginas;
        ConfirmacionPdfData metadatos;

        log.info("[Sendata] [3/8] Abriendo PDF con PDFBox...");
        try (PDDocument documento = PDDocument.load(new ByteArrayInputStream(bytes))) {
            numeroPaginas = documento.getNumberOfPages();
            log.info("[Sendata] [3/8] PDF cargado correctamente — {} página(s)", numeroPaginas);

            metadatos = extraerMetadatos(documento);
            log.info("[Sendata] [3/8] Metadatos extraídos — evento='{}', nombre='{}'",
                     metadatos.getEvento(), metadatos.getNombre());

            PDFRenderer renderer = new PDFRenderer(documento);
            for (int i = 0; i < numeroPaginas; i++) {
                log.debug("[Sendata] Renderizando página {} a {} DPI...", i + 1, DPI_RENDER);
                BufferedImage imagen = renderer.renderImageWithDPI(i, DPI_RENDER);
                log.debug("[Sendata] Página {} renderizada ({}x{} px)",
                          i + 1, imagen.getWidth(), imagen.getHeight());

                String codigo = decodificarCodigo(imagen);
                if (codigo != null) {
                    codigos.add(codigo);
                    log.info("[Sendata] Código encontrado en página {}: '{}'", i + 1, codigo);
                } else {
                    log.warn("[Sendata] Página {} — no se encontró código QR/barcode", i + 1);
                }

                imagenes.add(toJpegBytes(imagen));
                log.debug("[Sendata] Página {} convertida a JPEG ({} bytes)", i + 1, imagenes.get(imagenes.size() - 1).length);
            }
        } catch (IOException e) {
            log.error("[Sendata] Error al procesar el PDF: {}", e.getMessage(), e);
            throw new PdfValidacionException("No se pudo leer el PDF: " + e.getMessage());
        }

        log.info("[Sendata] [3/8] Extracción finalizada — {}/{} páginas con código",
                 codigos.size(), numeroPaginas);

        if (codigos.isEmpty()) {
            log.error("[Sendata] El PDF no contiene ningún código QR/barcode válido");
            throw new PdfValidacionException("El PDF no contiene ningún código de barras o QR válido");
        }
        
        //Aqui con un try catch. detectaria y no subiria a minio.
        
        try {
            // 4. Verificar QRs contra la tabla entrada (anti-fraude)
            log.info("[Sendata] [4/8] Validando {} código(s) contra BD (tabla entrada)...", codigos.size());
            validarQrsEnBD(codigos);
            log.info("[Sendata] [4/8] Validación contra BD OK — ningún QR es una entrada activa de plataforma");

            // 5. Anti-duplicados contra barcode_registro
            log.info("[Sendata] [5/8] Comprobando duplicados en barcode_registro...");
            barcodeRegistroService.verificarNoDuplicados(codigos);
            log.info("[Sendata] [5/8] Sin duplicados detectados");
        	
        }catch(Exception e) {
        	
        	throw new PdfValidacionException(e.getMessage());
        }
          
        

        // 6. Subir el PDF a MinIO
        log.info("[Sendata] [6/8] Subiendo PDF a MinIO...");
        String objectNamePdf = minioService.cargaArchivo(archivo, idUsuario);
        log.info("[Sendata] [6/8] PDF subido en MinIO: '{}'", objectNamePdf);

        // 7. Subir imágenes de páginas y generar URLs
        log.info("[Sendata] [7/8] Subiendo {} imagen(es) de páginas a MinIO...", imagenes.size());
        List<String> urls = new ArrayList<>();
        for (int i = 0; i < imagenes.size(); i++) {
            String objectNameImg = minioService.cargadeImagenes(imagenes.get(i), idUsuario, i);
            String url = minioService.generarurl(objectNameImg);
            urls.add(url);
            log.debug("[Sendata] Imagen página {} subida: '{}' → URL generada", i + 1, objectNameImg);
        }
        log.info("[Sendata] [7/8] {} imagen(es) subidas correctamente", urls.size());

        // 8. Crear Entradatmp y registrar barcodes
        log.info("[Sendata] [8/8] Creando registro Entradatmp en BD...");
        Entradatmp tmp = new Entradatmp(objectNamePdf, idUsuario, hash, numeroPaginas, LocalDate.now());
        Integer idEntradaTmp = entradatmpService.insertarEntradaTmp(tmp);
        log.info("[Sendata] [8/8] Entradatmp creada con id={}", idEntradaTmp);

        log.info("[Sendata] [8/8] Registrando {} barcode(s) para entradatmp id={}...", codigos.size(), idEntradaTmp);
        barcodeRegistroService.registrarBarcodes(codigos, idEntradaTmp, idUsuario);
        log.info("[Sendata] [8/8] Barcodes registrados correctamente");

        log.info("[Sendata] ── FIN procesarPdf OK — hash={}, páginas={}, códigos={} ──",
                 hash, numeroPaginas, codigos.size());

        return new FileResponse(codigos, urls, hash, null, metadatos);
    }

    // ── privados ───────────────────────────────────────────────────────────────

    private void validarArchivo(MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) {
            log.warn("[Sendata] Archivo nulo o vacío recibido");
            throw new PdfValidacionException("El archivo PDF no puede estar vacío");
        }
        String contentType = archivo.getContentType();
        log.debug("[Sendata] Validando archivo — contentType='{}', size={} bytes",
                  contentType, archivo.getSize());
        if (contentType == null || !contentType.equals("application/pdf")) {
            log.warn("[Sendata] ContentType inválido: '{}'", contentType);
            throw new PdfValidacionException("El archivo debe ser un PDF");
        }
        if (archivo.getSize() > MAX_SIZE_BYTES) {
            log.warn("[Sendata] Archivo demasiado grande: {} bytes (máx {})", archivo.getSize(), MAX_SIZE_BYTES);
            throw new PdfValidacionException("El archivo no puede superar los 10 MB");
        }
    }

    private void validarQrsEnBD(List<String> codigos) {
        log.debug("[Sendata] Iniciando validación QR en BD para {} código(s)", codigos.size());
        for (String codigo : codigos) {
            log.debug("[Sendata] Consultando entrada para QR: '{}'", codigo);
            entradaDao.selectEntradabyqr(codigo).ifPresent(entrada -> {
                log.warn("[Sendata] QR ya registrado como entrada id={} estado='{}': '{}'",
                         entrada.getEntrada_id(), entrada.getEstado_entrada(), codigo);
                throw new PdfValidacionException(
                    "El código '" + codigo + "' ya está registrado como entrada activa en la plataforma");
            });
            log.debug("[Sendata] QR '{}' no encontrado en tabla entrada — OK", codigo);
        }
    }

    private ConfirmacionPdfData extraerMetadatos(PDDocument documento) {
        PDDocumentInformation info = documento.getDocumentInformation();
        log.debug("[Sendata] Metadatos raw — title='{}', subject='{}', author='{}', creator='{}', creationDate='{}'",
                  info.getTitle(), info.getSubject(), info.getAuthor(),
                  info.getCreator(), info.getCreationDate());
        
        //VALIDACION DEL AUTOR.
        if(!info.getAuthor().equalsIgnoreCase(Author.Ticketmaster.name())) {
        	//Aqui autor invalido.
        	 log.warn("Author del pdf desconocido");
        	 throw new PdfValidacionException(
        			 "El pdf que se ha subido no es auntentico");
        }
        
        //VALIDACION DEL CREADOR
        if(info.getCreator() == null ||   !info.getCreator().equalsIgnoreCase("TCT 5.24.0008") ) {
        	log.error("El pdf no tiene un sofware oficial de certificacion");       
       	 throw new PdfValidacionException(
    			 "El pdf que se ha subido no es auntentico por falta de creator.");
        }
        
        //Validacion de cración.
        
        Calendar fechacreacion = info.getCreationDate();
        if(fechacreacion == null) {
        	throw new PdfValidacionException("El PDF no contiene fecha de creación");
        }
        
        LocalDateTime creationDate = fechacreacion.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        
        if (creationDate.isAfter(LocalDateTime.now())) {
            throw new PdfValidacionException("La fecha de creación del PDF no es válida");
        }

        Calendar fechamodificacion = info.getModificationDate();
        
        if(fechamodificacion!= null ) {
            LocalDateTime modificationDate = fechamodificacion.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
            
            if (!modificationDate.equals(fechacreacion)) {
                log.warn("El PDF ha sido modificado. creation='{}', modification='{}'",
                		creationDate, modificationDate);

                throw new PdfValidacionException("El PDF ha sido modificado tras su creación");
            }
        	
        }
        
        String evento = info.getTitle() != null   ? info.getTitle()
                      : info.getSubject() != null ? info.getSubject()
                      : "Sin título";
        String nombre = info.getAuthor() != null ? info.getAuthor() : "Sin autor";
        log.debug("[Sendata] Metadatos mapeados — evento='{}', nombre='{}'", evento, nombre);
        return new ConfirmacionPdfData(evento, nombre);
    }

    private Integer idUsuarioActual() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        log.debug("[Sendata] Resolviendo usuario por email: '{}'", email);
        return userDao.selectUserbyemail(email)
            .orElseThrow(() -> {
                log.error("[Sendata] Usuario no encontrado para email: '{}'", email);
                return new PdfValidacionException("Usuario no encontrado");
            })
            .getId_usuario();
    }

    private String decodificarCodigo(BufferedImage imagen) {
        BinaryBitmap bitmap = new BinaryBitmap(
            new HybridBinarizer(new BufferedImageLuminanceSource(imagen))
        );
        try {
            String texto = new MultiFormatReader().decode(bitmap).getText();
            log.debug("[Sendata] decodificarCodigo → '{}'", texto);
            return texto;
        } catch (NotFoundException e) {
            log.debug("[Sendata] decodificarCodigo → sin código en esta imagen");
            return null;
        }
    }

    private byte[] toJpegBytes(BufferedImage imagen) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(imagen, "jpg", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            log.error("[Sendata] Error al convertir imagen a JPEG: {}", e.getMessage(), e);
            throw new PdfValidacionException("Error al convertir imagen: " + e.getMessage());
        }
    }
}

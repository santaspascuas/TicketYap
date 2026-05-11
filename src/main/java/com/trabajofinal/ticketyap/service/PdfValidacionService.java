package com.trabajofinal.ticketyap.service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Optional;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.trabajofinal.ticketyap.dao.EntradaDao;
import com.trabajofinal.ticketyap.exceptions.PdfValidacionException;
import com.trabajofinal.ticketyap.modelos.Entrada;
import com.trabajofinal.ticketyap.modelos.DTO.PdfValidacionResultado;

@Service
public class PdfValidacionService {

    private static final Logger log = LoggerFactory.getLogger(PdfValidacionService.class);
    private static final int DPI_RENDER = 150;

    private final EntradaDao entradaDao;

    public PdfValidacionService(EntradaDao entradaDao) {
        this.entradaDao = entradaDao;
    }

    public PdfValidacionResultado validar(MultipartFile archivo) {
        validarArchivo(archivo);
        String codigoQr = extraerQr(archivo);
        return validarContraBaseDatos(codigoQr);
    }

    // --- privados ---

    private void validarArchivo(MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) {
            throw new PdfValidacionException("El archivo PDF no puede estar vacío");
        }
        String contentType = archivo.getContentType();
        if (contentType == null || !contentType.equals("application/pdf")) {
            throw new PdfValidacionException("El archivo debe ser un PDF");
        }
    }

    private String extraerQr(MultipartFile archivo) {
        try (PDDocument documento = PDDocument.load(archivo.getInputStream())) {
            PDFRenderer renderer = new PDFRenderer(documento);
            int paginas = documento.getNumberOfPages();
            for (int i = 0; i < paginas; i++) {
                BufferedImage imagen = renderer.renderImageWithDPI(i, DPI_RENDER);
                String qr = decodificarQr(imagen);
                if (qr != null) {
                    log.info("QR encontrado en página {}", i + 1);
                    return qr;
                }
            }
        } catch (IOException e) {
            throw new PdfValidacionException("No se pudo leer el PDF: " + e.getMessage());
        }
        throw new PdfValidacionException("No se encontró código QR en el PDF");
    }

    private String decodificarQr(BufferedImage imagen) {
        BinaryBitmap bitmap = new BinaryBitmap(
            new HybridBinarizer(new BufferedImageLuminanceSource(imagen))
        );
        try {
            return new MultiFormatReader().decode(bitmap).getText();
        } catch (NotFoundException e) {
            return null;
        }
    }

    private PdfValidacionResultado validarContraBaseDatos(String codigoQr) {
        Optional<Entrada> resultado = entradaDao.selectEntradabyqr(codigoQr);

        if (resultado.isEmpty()) {
            log.warn("QR no registrado: {}", codigoQr);
            return PdfValidacionResultado.invalido(codigoQr, "QR no registrado en el sistema");
        }

        Entrada entrada = resultado.get();
        log.info("Entrada encontrada: id={}, estado={}", entrada.getEntrada_id(), entrada.getEstado_entrada());
        return PdfValidacionResultado.valido(codigoQr, entrada);
    }
}

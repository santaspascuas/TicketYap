package com.trabajofinal.ticketyap.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.trabajofinal.ticketyap.dao.ImagenEventoTmpDao;
import com.trabajofinal.ticketyap.exceptions.EventoImagenException;
import com.trabajofinal.ticketyap.modelos.DTO.ImagenEventoDto;
import com.trabajofinal.ticketyap.modelos.ImagenEventoTmp;
import com.trabajofinal.ticketyap.service.MinioService;

@Service
public class EventoImagenService {

    private final ImagenEventoTmpDao imagentmpdao;
    private final MinioService minioservice;
    private static final Logger log = LoggerFactory.getLogger(EventoImagenService.class);

    
    //Inicializamos

    public EventoImagenService(ImagenEventoTmpDao imagentmpdao,MinioService minioservice){
        this.imagentmpdao = imagentmpdao;
        this. minioservice = minioservice;
    }

    @Transactional
    public String  InsertarEventoImagen(MultipartFile[] files , Integer id_Evento){
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("[{} Inicio del metodo]", method);
        log.debug("se recibe el parametro del evento:{}", id_Evento);
        //La idea es poder insertar las imagenes de evento.
        if(files.length == 0){
            log.error("Error no se envia fichero");
            throw new EventoImagenException("No se ha enviado ninguna imagen");
        }

        //Si manda muchas imagenes.
        for (MultipartFile file : files) {
            // creo file por cada imagenes subidas
            try {
                //Insertamos la imagen en minio
                String objectName = minioservice.cargaImagenesEvento(file.getBytes());
                log.debug("Insertamos imagen en minio:{}",objectName);

                if(imagentmpdao.buscarImagenporidPrincipal(objectName).isPresent()){
                    log.error("Error a la hora de insertar la imagen en la data");
                    throw new EventoImagenException("Error  la imagen ya esta insertada en la database");
                }

                //Ahora seria crear el evento para la database
                ImagenEventoTmp imagentmevento = new ImagenEventoTmp(objectName, id_Evento, null);
                log.debug("Se crea eventoImagen: {}",imagentmevento.toString());

                // Guardamos en la database
                imagentmpdao.guardarImagenEventotmp(imagentmevento);
            }catch (Exception e) {
                throw new EventoImagenException("Error al procesar la imagen: " + file.getOriginalFilename());
            }
        }
        return "Se ha insertado la imagen en la database";
    }

    @Transactional
    public String EliminarImagenEventobyEvento(Integer id_evento){
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("[{} Inicio del metodo]", method);
        log.debug("se recibe el parametro del evento:{}", id_evento);

        if(!imagentmpdao.eliminarImagenEventotmp(id_evento)){
            log.error("Imagen no esta en la database para eliminar");
            throw new EventoImagenException("Error  no se ha borrado la imagen de la database");
        }
        imagentmpdao.eliminarImagenEventotmp(id_evento);
        return "Se ha borrado correctamente la imagen del evento";
    }

    public List<ImagenEventoTmp> buscarImagenPorEvento(Integer evento){
        return imagentmpdao.obtenerImagenporID(evento);
    }

    @Transactional
    public List<ImagenEventoDto>buscarTodasImagenes(){
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("[{} Inicio del metodo]", method);

        List<ImagenEventoTmp> imagenes = imagentmpdao.obtenerAllImagenEvento();
        log.debug("Lista de las imagenes");
        

        if(imagenes.isEmpty()){
            throw new EventoImagenException("No se han encontrado imagenes en la database");
        }
        //Aqui seria trasnformarlo para obtener las urls
        List<ImagenEventoDto> urlsPublicas = new ArrayList<>();
        //Vamos a sacar todas las urls publicas de las imagenes
        for(ImagenEventoTmp imagen : imagenes){
            String urlPublica = minioservice.generarurl(imagen.getPath_imagen(), MinioService.BUCKET_IMAGEN);
            //La idea es crear el dto de retorno para cada imagen son su url publica
            ImagenEventoDto imagenDto = new ImagenEventoDto(
                urlPublica,
                imagen.getEvento_id(),
                imagen.getFecha_publicacion(),
                imagen.getId_entrada());

            urlsPublicas.add(imagenDto);
        }
        return urlsPublicas;
    }
















    



}

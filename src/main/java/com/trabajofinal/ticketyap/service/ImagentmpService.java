package com.trabajofinal.ticketyap.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.trabajofinal.ticketyap.dao.ImagenTmpDao;
import com.trabajofinal.ticketyap.modelos.Imagentmp;

@Service
public class ImagentmpService  {

    //Inyectamos la dependencia dao
    private final ImagenTmpDao imagentmpdao;
    private static final Logger log = LoggerFactory.getLogger(ImagentmpService.class);

    public ImagentmpService(ImagenTmpDao imagentmpdao){
        this.imagentmpdao = imagentmpdao;
    }

    //Terminar las funcionesº
    public void guardarImagenTemMinio (Imagentmp imagentmp){
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("[{} Inicio del metodo]", method);
        log.debug("Recibimos la imagentmp:", imagentmp);
        imagentmpdao.guardarImagenTmp(imagentmp);
    }

    //Eliminar imagenes por id de entrada

    public boolean eliminarImagenesPorIdEntrada(Integer id_entrada){

        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("[{} Inicio del metodo]", method);
        log.debug("Recibimos la id_entrada:", id_entrada);

        Boolean resultado = imagentmpdao.eliminarImagenTmp(id_entrada);
        
        if(resultado){
            System.out.println("Imagenes eliminadas correctamente");
            return true;
        }
        return false;
    }

    public List<String> obtenerImagenesPorIdEntrada(Integer id_pdf){
        return imagentmpdao.obtenerImagenTmpPorId(id_pdf)
        .stream()
        .map(Imagentmp::getPath_imagen)
        .toList();
    }









}

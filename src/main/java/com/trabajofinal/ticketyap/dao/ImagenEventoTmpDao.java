package com.trabajofinal.ticketyap.dao;

import java.util.List;
import java.util.Optional;

import com.trabajofinal.ticketyap.modelos.ImagenEventoTmp;

public interface ImagenEventoTmpDao {

    public void guardarImagenEventotmp(ImagenEventoTmp imagenevento);
    boolean eliminarImagenEventotmp(Integer id_imagen);
    List<ImagenEventoTmp> obtenerImagenporID(Integer id_evento);
    Optional<ImagenEventoTmp>buscarImagenporidPrincipal(String path_name);
    List<ImagenEventoTmp> obtenerAllImagenEvento();
}

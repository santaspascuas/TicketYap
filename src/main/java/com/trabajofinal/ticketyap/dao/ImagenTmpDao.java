package com.trabajofinal.ticketyap.dao;

import java.util.List;

import com.trabajofinal.ticketyap.modelos.Imagentmp;

public interface ImagenTmpDao {
    public void guardarImagenTmp(Imagentmp imagentmp);
    boolean eliminarImagenTmp(Integer id_entrada);
    List<Imagentmp> obtenerImagenTmpPorId(Integer id_entrada);
}

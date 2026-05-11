package com.trabajofinal.ticketyap.dao;

import java.util.List;

import com.trabajofinal.ticketyap.modelos.TipoEvento;

public interface TipoEventoDao {

    Integer insertarTipoEvento(TipoEvento tipo);
    List<TipoEvento> listarTipoEvento();
    Integer deleteTipoEvento(Integer id);
    Boolean ConfirmaEvento(String nombre);
}

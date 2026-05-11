package com.trabajofinal.ticketyap.dao;

import java.util.List;
import java.util.Optional;

import com.trabajofinal.ticketyap.modelos.Localizacion;

public interface LocalizacionDao {

    List<Localizacion> selectLocalizacion();
    Integer insertarLocalizacion(Localizacion local);
    Integer deleteLocalizacion(Integer localizacion);
    Integer UpdateLocalizacion(Localizacion local);
    Optional<Localizacion>buscarlocalizacion(Integer localizacion);
    Optional<Localizacion>buscarpornombre(String local);
}

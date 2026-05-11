package com.trabajofinal.ticketyap.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.trabajofinal.ticketyap.modelos.CombinaLocalizacionEvento;
import com.trabajofinal.ticketyap.modelos.Eventos;

public interface EventoDao {
    List<Eventos>selectEventos();
    Optional<Eventos>selectEventoByid( Integer id);
    Integer deleteEvento(Integer id);
    Integer insertarEvento(Eventos eventoInsertado);
    Optional<Eventos>selectEventoByname( String nombre);
    List<CombinaLocalizacionEvento>combinaEvento(Integer id);
    Integer updateEvento(Eventos eventoInsertado);
    Optional<Eventos>selectEventoByUUID(UUID uuid);
    BigDecimal obtenerPrecioEntrada(Integer id);

}

package com.trabajofinal.ticketyap.dao;

import java.util.List;
import java.util.Optional;

import com.trabajofinal.ticketyap.modelos.Oferta;
import com.trabajofinal.ticketyap.modelos.DTO.OfertaDTO;

public interface OfertaDao {

    Integer insertar(Oferta oferta);

    Optional<OfertaDTO> selectById(Integer idOferta);

    List<OfertaDTO> selectPorEntrada(Integer idEntrada);

    List<OfertaDTO> selectPorComprador(Integer idComprador);

    Integer actualizarEstado(Integer idOferta, String estado);

    Integer rechazarPendientesPorEntrada(Integer idEntrada);

    boolean existeOfertaPendiente(Integer idEntrada, Integer idComprador);
}

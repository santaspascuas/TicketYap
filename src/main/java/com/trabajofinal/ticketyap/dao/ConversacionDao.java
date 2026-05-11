package com.trabajofinal.ticketyap.dao;

import java.util.List;
import java.util.Optional;

import com.trabajofinal.ticketyap.modelos.Conversacion;
import com.trabajofinal.ticketyap.modelos.Mensaje;
import com.trabajofinal.ticketyap.modelos.DTO.ConversacionDTO;
import com.trabajofinal.ticketyap.modelos.DTO.MensajeDTO;

public interface ConversacionDao {

    Integer insertar(Conversacion conversacion);

    Optional<Conversacion> selectById(Integer idConversacion);

    Optional<Conversacion> selectPorEntradaYComprador(Integer idEntrada, Integer idComprador);

    List<Conversacion> selectPorUsuario(Integer idUsuario);

    // Devuelve conversaciones enriquecidas con nombres, último mensaje y no leídos
    List<ConversacionDTO> selectResumenPorUsuario(Integer idUsuario);

    Integer insertarMensaje(Mensaje mensaje);

    // Mensajes con es_propio calculado según el usuario que consulta
    List<MensajeDTO> selectMensajes(Integer idConversacion, Integer idUsuario);

    Optional<MensajeDTO> selectUltimoMensaje(Integer idConversacion);
}

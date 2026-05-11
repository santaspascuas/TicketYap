package com.trabajofinal.ticketyap.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.trabajofinal.ticketyap.dao.ConversacionDao;
import com.trabajofinal.ticketyap.dao.EntradaDao;
import com.trabajofinal.ticketyap.dao.UserDao;
import com.trabajofinal.ticketyap.exceptions.ConversacionException;
import com.trabajofinal.ticketyap.exceptions.NotFoundException;
import com.trabajofinal.ticketyap.modelos.Conversacion;
import com.trabajofinal.ticketyap.modelos.Entrada;
import com.trabajofinal.ticketyap.modelos.Mensaje;
import com.trabajofinal.ticketyap.modelos.DTO.ConversacionDTO;
import com.trabajofinal.ticketyap.modelos.DTO.MensajeDTO;
import com.trabajofinal.ticketyap.modelos.DTO.MensajeRequest;

@Service
public class ConversacionService {

    private static final Logger log = LoggerFactory.getLogger(ConversacionService.class);

    private final ConversacionDao conversacionDao;
    private final EntradaDao entradaDao;
    private final UserDao userDao;

    public ConversacionService(ConversacionDao conversacionDao, EntradaDao entradaDao, UserDao userDao) {
        this.conversacionDao = conversacionDao;
        this.entradaDao      = entradaDao;
        this.userDao         = userDao;
    }

    public ConversacionDTO obtenerOCrearConversacion(Integer idEntrada) {
        Integer idComprador = idUsuarioActual();
        Entrada entrada = obtenerEntrada(idEntrada);

        if (entrada.getId_vendedor().equals(idComprador)) {
            throw new ConversacionException("El vendedor no puede abrir una conversación sobre su propia entrada");
        }

        if (conversacionDao.selectPorEntradaYComprador(idEntrada, idComprador).isEmpty()) {
            conversacionDao.insertar(new Conversacion(idEntrada, idComprador, entrada.getId_vendedor()));
            log.info("Conversación creada: entrada={}, comprador={}", idEntrada, idComprador);
        }

        Conversacion conv = conversacionDao.selectPorEntradaYComprador(idEntrada, idComprador)
            .orElseThrow(() -> new ConversacionException("Error al crear la conversación"));

        List<MensajeDTO> mensajes = conversacionDao.selectMensajes(conv.getId_conversacion(), idComprador);
        return toDTO(conv, mensajes, idComprador);
    }

    public MensajeDTO enviarMensaje(Integer idConversacion, MensajeRequest request) {
        Integer idEmisor = idUsuarioActual();
        Conversacion conversacion = obtenerConversacion(idConversacion);

        validarParticipante(conversacion, idEmisor);
        validarContenido(request.getContenido());

        conversacionDao.insertarMensaje(new Mensaje(idConversacion, idEmisor, request.getContenido()));
        log.info("Mensaje enviado en conversacion {} por usuario {}", idConversacion, idEmisor);

        return conversacionDao.selectUltimoMensaje(idConversacion)
            .orElseThrow(() -> new ConversacionException("Error al recuperar el mensaje enviado"));
    }

    // Lista ligera con resumen (para el sidebar de conversaciones)
    public List<ConversacionDTO> obtenerMisConversaciones() {
        Integer idUsuario = idUsuarioActual();
        return conversacionDao.selectResumenPorUsuario(idUsuario);
    }

    // Detalle con mensajes completos (para el hilo de chat)
    public List<MensajeDTO> obtenerMensajes(Integer idConversacion) {
        Integer idUsuario = idUsuarioActual();
        Conversacion conv = obtenerConversacion(idConversacion);
        validarParticipante(conv, idUsuario);
        return conversacionDao.selectMensajes(idConversacion, idUsuario);
    }

    public ConversacionDTO obtenerConversacionConMensajes(Integer idConversacion) {
        Integer idUsuario = idUsuarioActual();
        Conversacion conv = obtenerConversacion(idConversacion);
        validarParticipante(conv, idUsuario);
        List<MensajeDTO> mensajes = conversacionDao.selectMensajes(idConversacion, idUsuario);
        return toDTO(conv, mensajes, idUsuario);
    }

    // --- helpers privados ---

    private Integer idUsuarioActual() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userDao.selectUserbyemail(email)
            .orElseThrow(() -> new NotFoundException("Usuario autenticado no encontrado"))
            .getId_usuario();
    }

    private String nombreUsuario(Integer idUsuario) {
        return userDao.selectusuariobyid(idUsuario)
            .map(u -> u.getNombre())
            .orElse("Usuario");
    }

    private Entrada obtenerEntrada(Integer idEntrada) {
        return entradaDao.selectEntradaById(idEntrada)
            .orElseThrow(() -> new ConversacionException("Entrada no encontrada: " + idEntrada));
    }

    private Conversacion obtenerConversacion(Integer idConversacion) {
        return conversacionDao.selectById(idConversacion)
            .orElseThrow(() -> new ConversacionException("Conversación no encontrada: " + idConversacion));
    }

    private void validarParticipante(Conversacion conversacion, Integer idUsuario) {
        boolean esParticipante = conversacion.getId_comprador().equals(idUsuario)
            || conversacion.getId_vendedor().equals(idUsuario);
        if (!esParticipante) {
            throw new ConversacionException("No tienes acceso a esta conversación");
        }
    }

    private void validarContenido(String contenido) {
        if (contenido == null || contenido.isBlank()) {
            throw new ConversacionException("El mensaje no puede estar vacío");
        }
    }

    private ConversacionDTO toDTO(Conversacion c, List<MensajeDTO> mensajes, Integer idUsuario) {
        String nombreComprador = nombreUsuario(c.getId_comprador());
        String nombreVendedor  = nombreUsuario(c.getId_vendedor());
        MensajeDTO ultimo = mensajes.isEmpty() ? null : mensajes.get(mensajes.size() - 1);

        return new ConversacionDTO(
            c.getId_conversacion(),
            c.getId_entrada(),
            c.getId_comprador(),
            c.getId_vendedor(),
            nombreComprador,
            nombreVendedor,
            c.getFecha_creacion(),
            ultimo != null ? ultimo.getContenido() : null,
            ultimo != null ? ultimo.getFecha_envio() : null,
            0,
            mensajes
        );
    }
}

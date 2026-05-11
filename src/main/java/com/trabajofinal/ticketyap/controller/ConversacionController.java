package com.trabajofinal.ticketyap.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.trabajofinal.ticketyap.modelos.DTO.ConversacionDTO;
import com.trabajofinal.ticketyap.modelos.DTO.MensajeDTO;
import com.trabajofinal.ticketyap.modelos.DTO.MensajeRequest;
import com.trabajofinal.ticketyap.response.ApiResponse;
import com.trabajofinal.ticketyap.response.ResponseUtil;
import com.trabajofinal.ticketyap.service.ConversacionService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/conversacion")
public class ConversacionController {

    private final ConversacionService conversacionService;

    public ConversacionController(ConversacionService conversacionService) {
        this.conversacionService = conversacionService;
    }

    @PostMapping("/entrada/{idEntrada}")
    public ResponseEntity<ApiResponse<ConversacionDTO>> obtenerOCrear(
            HttpServletRequest request,
            @PathVariable Integer idEntrada) {

        ConversacionDTO resultado = conversacionService.obtenerOCrearConversacion(idEntrada);
        return ResponseEntity.ok(
            ResponseUtil.sucess(resultado, "Conversación lista", request.getRequestURI()));
    }

    @PostMapping("/{id}/mensaje")
    public ResponseEntity<ApiResponse<MensajeDTO>> enviarMensaje(
            HttpServletRequest request,
            @PathVariable Integer id,
            @RequestBody MensajeRequest body) {

        MensajeDTO resultado = conversacionService.enviarMensaje(id, body);
        return ResponseEntity.ok(
            ResponseUtil.sucess(resultado, "Mensaje enviado", request.getRequestURI()));
    }

    @GetMapping("/mias")
    public ResponseEntity<ApiResponse<List<ConversacionDTO>>> misConversaciones(HttpServletRequest request) {
        List<ConversacionDTO> resultado = conversacionService.obtenerMisConversaciones();
        return ResponseEntity.ok(
            ResponseUtil.sucess(resultado, "Mis conversaciones", request.getRequestURI()));
    }

    // Endpoint que el frontend llama para cargar el hilo de mensajes
    @GetMapping("/{id}/mensajes")
    public ResponseEntity<ApiResponse<List<MensajeDTO>>> mensajes(
            HttpServletRequest request,
            @PathVariable Integer id) {

        List<MensajeDTO> resultado = conversacionService.obtenerMensajes(id);
        return ResponseEntity.ok(
            ResponseUtil.sucess(resultado, "Mensajes de la conversación", request.getRequestURI()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ConversacionDTO>> detalle(
            HttpServletRequest request,
            @PathVariable Integer id) {

        ConversacionDTO resultado = conversacionService.obtenerConversacionConMensajes(id);
        return ResponseEntity.ok(
            ResponseUtil.sucess(resultado, "Detalle de conversación", request.getRequestURI()));
    }
}

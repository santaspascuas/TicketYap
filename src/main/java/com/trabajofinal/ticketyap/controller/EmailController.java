package com.trabajofinal.ticketyap.controller;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trabajofinal.ticketyap.events.RecuperarPasswordEvent;
import com.trabajofinal.ticketyap.modelos.RecoverPassword;
import com.trabajofinal.ticketyap.response.ApiResponse;
import com.trabajofinal.ticketyap.response.ResponseUtil;

@RestController
@RequestMapping("/email")
public class EmailController {

    private final ApplicationEventPublisher eventPublisher;

    public EmailController(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @PostMapping("/send")
    public ResponseEntity<ApiResponse<Object>> enviarhtml(@RequestBody RecoverPassword emailPassword) {
        eventPublisher.publishEvent(new RecuperarPasswordEvent(emailPassword.getEmail()));
        return ResponseEntity.ok(
            ResponseUtil.sucess(null, "Solicitud de recuperación de contraseña enviada", null)
        );
    }
}

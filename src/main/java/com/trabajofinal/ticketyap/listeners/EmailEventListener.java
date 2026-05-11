package com.trabajofinal.ticketyap.listeners;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.trabajofinal.ticketyap.events.CarritoConItemsEvent;
import com.trabajofinal.ticketyap.events.CompraCompletadaEvent;
import com.trabajofinal.ticketyap.events.NuevaOfertaEvent;
import com.trabajofinal.ticketyap.events.RecuperarPasswordEvent;
import com.trabajofinal.ticketyap.events.RegistroUsuarioEvent;
import com.trabajofinal.ticketyap.service.EmailService;

import jakarta.mail.MessagingException;

@Component
public class EmailEventListener {

    private static final Logger log = LoggerFactory.getLogger(EmailEventListener.class);
    private static final long COOLDOWN_MINUTOS = 90;

    private final EmailService emailService;
    private final ConcurrentHashMap<Integer, LocalDateTime> ultimoRecordatorio = new ConcurrentHashMap<>();

    public EmailEventListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @Async
    @EventListener
    public void onRegistroUsuario(RegistroUsuarioEvent event) {
        try {
            emailService.enviarCorreoRegistro(event.email());
        } catch (MessagingException e) {
            log.error("Error enviando correo de registro a {}: {}", event.email(), e.getMessage());
        }
    }

    @Async
    @EventListener
    public void onRecuperarPassword(RecuperarPasswordEvent event) {
        try {
            emailService.enviarCorreoRecuperarPassword(event.email());
        } catch (MessagingException e) {
            log.error("Error enviando correo de recuperación a {}: {}", event.email(), e.getMessage());
        }
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onCompraCompletada(CompraCompletadaEvent event) {
        try {
            emailService.enviarCorreoCompra(event.email());
        } catch (MessagingException e) {
            log.error("Error enviando correo de compra a {}: {}", event.email(), e.getMessage());
        }
    }

    @Async
    @EventListener
    public void onCarritoConItems(CarritoConItemsEvent event) {
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime ultimo = ultimoRecordatorio.get(event.idUsuario());
        if (ultimo != null && ultimo.plusMinutes(COOLDOWN_MINUTOS).isAfter(ahora)) {
            log.debug("Recordatorio de carrito omitido para usuario {} (cooldown activo)", event.idUsuario());
            return;
        }
        try {
            emailService.enviarCorreoCarritoAbandonado(event.email(), event.nombre(), event.numItems());
            ultimoRecordatorio.put(event.idUsuario(), ahora);
        } catch (Exception e) {
            log.error("Error enviando recordatorio de carrito a {}: {}", event.email(), e.getMessage());
        }
    }

    @Async
    @EventListener
    public void onNuevaOferta(NuevaOfertaEvent event) {
        try {
            emailService.enviarCorreoNuevaOferta(
                event.emailVendedor(),
                event.nombreComprador(),
                event.precioOferta(),
                event.idEntrada()
            );
        } catch (MessagingException e) {
            log.error("Error enviando correo de oferta a {}: {}", event.emailVendedor(), e.getMessage());
        }
    }
}

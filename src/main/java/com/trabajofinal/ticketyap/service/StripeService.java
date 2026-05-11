package com.trabajofinal.ticketyap.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import com.trabajofinal.ticketyap.events.CompraCompletadaEvent;
import com.trabajofinal.ticketyap.exceptions.NotFoundException;
import com.trabajofinal.ticketyap.modelos.DTO.ResponseBuy;
import com.trabajofinal.ticketyap.modelos.DTO.UserDto;

@Service
public class StripeService {

    private static final Logger log = LoggerFactory.getLogger(StripeService.class);

    private final EntradaService entradaservice;
    private final UserService usuarioservicio;
    private final ApplicationEventPublisher eventPublisher;

    public StripeService(
            @Value("${stripe.secret-key}") String stripeSecretKey,
            EntradaService entradaservice,
            UserService usuarioservicio,
            ApplicationEventPublisher eventPublisher) {
        this.entradaservice = entradaservice;
        this.usuarioservicio = usuarioservicio;
        this.eventPublisher = eventPublisher;
        Stripe.apiKey = stripeSecretKey;
    }

    // Crea un PaymentIntent por el total del carrito y guarda los IDs en metadata.
    public String responsePago(List<ResponseBuy> items, Authentication authentication) throws StripeException {
        UserDto usuario = usuarioservicio.obtenerUsuarioporEmail(authentication.getName());

        BigDecimal total = items.stream()
                .map(ResponseBuy::getPrecio_venta)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        String entradaIds = items.stream()
                .map(i -> String.valueOf(i.getId_entrada()))
                .collect(Collectors.joining(","));

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(total.multiply(new BigDecimal(100)).longValue())
                .setCurrency("eur")
                .setDescription("Pago de entradas — TicketYap")
                .addPaymentMethodType("card")
                .putMetadata("entrada_ids", entradaIds)
                .putMetadata("usuario_id", String.valueOf(usuario.getId_usuario()))
                .build();

        PaymentIntent paymentIntent = PaymentIntent.create(params);
        log.info("PaymentIntent {} creado para usuario {} — entradas [{}]",
                paymentIntent.getId(), usuario.getId_usuario(), entradaIds);
        return paymentIntent.getClientSecret();
    }

    // Verifica el PaymentIntent con Stripe y marca las entradas de su metadata como compradas.
    // Los IDs de entrada vienen del servidor (metadata), nunca del cliente.
    @Transactional
    public String LanzarCorreodeCompra(String paymentIntentId, Authentication authentication) throws StripeException {
        PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);

        if (!"succeeded".equals(paymentIntent.getStatus())) {
            throw new IllegalStateException(
                "El pago no ha sido completado. Estado: " + paymentIntent.getStatus());
        }

        String correo = authentication.getName();
        UserDto usuario = usuarioservicio.obtenerUsuarioporEmail(correo);

        String metaUsuarioId = paymentIntent.getMetadata().get("usuario_id");
        if (metaUsuarioId == null || !metaUsuarioId.equals(String.valueOf(usuario.getId_usuario()))) {
            throw new org.springframework.security.access.AccessDeniedException(
                "Este PaymentIntent no pertenece al usuario autenticado");
        }

        String entradaIdsMeta = paymentIntent.getMetadata().get("entrada_ids");
        if (entradaIdsMeta == null || entradaIdsMeta.isBlank()) {
            throw new NotFoundException("No se encontraron entradas asociadas a este pago");
        }

        List<Integer> entradaIds = Arrays.stream(entradaIdsMeta.split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        for (Integer idEntrada : entradaIds) {
            Integer resultado = entradaservice.ActualizarEstadoCompraEntrada(
                    idEntrada, usuario.getId_usuario(), "Comprado");
            if (resultado != 1) {
                throw new IllegalStateException(
                        "No se pudo actualizar el estado de la entrada " + idEntrada);
            }
        }

        log.info("Compra confirmada para usuario {} — entradas {} — PaymentIntent {}",
                usuario.getId_usuario(), entradaIds, paymentIntentId);
        eventPublisher.publishEvent(new CompraCompletadaEvent(correo, entradaIds));
        return "Compra confirmada. Recibirás un correo de confirmación en breve.";
    }
}

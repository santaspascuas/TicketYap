package com.trabajofinal.ticketyap.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stripe.exception.StripeException;
import com.trabajofinal.ticketyap.modelos.DTO.ConfirmarCompraRequest;
import com.trabajofinal.ticketyap.modelos.DTO.ResponseBuy;
import com.trabajofinal.ticketyap.response.ApiResponse;
import com.trabajofinal.ticketyap.response.ResponseUtil;
import com.trabajofinal.ticketyap.service.StripeService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/pago")
public class RecibirPagosController {

    private final StripeService stripervice;

    public RecibirPagosController(StripeService stripervice) {
        this.stripervice = stripervice;
    }

    @PostMapping("/solicitado")
    public ResponseEntity<ApiResponse<Object>> comprobaciondeCompra(
            @RequestBody List<ResponseBuy> items,
            HttpServletRequest request,
            Authentication authentication) throws StripeException {
        return ResponseEntity.ok(
            ResponseUtil.sucess(
                stripervice.responsePago(items, authentication),
                "PaymentIntent creado correctamente",
                request.getRequestURI())
        );
    }

    // Recibe el PaymentIntent ID del cliente. Los IDs de entrada se leen
    // de la metadata del PaymentIntent (servidor), nunca del cuerpo del cliente.
    @PostMapping("/compra")
    public ResponseEntity<ApiResponse<Object>> lanzarEmailCompra(
            @RequestBody ConfirmarCompraRequest body,
            HttpServletRequest request,
            Authentication authentication) throws StripeException {
        return ResponseEntity.ok(
            ResponseUtil.sucess(
                stripervice.LanzarCorreodeCompra(body.getPaymentIntentId(), authentication),
                "Compra confirmada",
                request.getRequestURI())
        );
    }
}

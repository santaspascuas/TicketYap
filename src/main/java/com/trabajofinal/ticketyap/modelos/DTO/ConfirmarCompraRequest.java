package com.trabajofinal.ticketyap.modelos.DTO;

public class ConfirmarCompraRequest {
    private String paymentIntentId;

    public String getPaymentIntentId() { return paymentIntentId; }
    public void setPaymentIntentId(String paymentIntentId) { this.paymentIntentId = paymentIntentId; }
}

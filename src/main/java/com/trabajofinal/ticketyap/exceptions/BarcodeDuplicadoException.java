package com.trabajofinal.ticketyap.exceptions;

import java.time.LocalDateTime;

public class BarcodeDuplicadoException extends RuntimeException {

    private final String hashDuplicado;
    private final LocalDateTime fechaRegistroOriginal;

    public BarcodeDuplicadoException(String hashDuplicado, LocalDateTime fechaRegistroOriginal) {
        super("Ticket duplicado detectado: este código ya fue registrado el "
              + fechaRegistroOriginal + ". Posible intento de venta múltiple.");
        this.hashDuplicado          = hashDuplicado;
        this.fechaRegistroOriginal  = fechaRegistroOriginal;
    }

    public String getHashDuplicado()                  { return hashDuplicado; }
    public LocalDateTime getFechaRegistroOriginal()   { return fechaRegistroOriginal; }
}

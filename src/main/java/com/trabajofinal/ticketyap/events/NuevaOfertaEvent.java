package com.trabajofinal.ticketyap.events;

import java.math.BigDecimal;

public record NuevaOfertaEvent(
    String emailVendedor,
    String nombreComprador,
    BigDecimal precioOferta,
    Integer idEntrada
) {}

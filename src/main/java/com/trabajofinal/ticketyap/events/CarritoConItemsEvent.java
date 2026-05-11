package com.trabajofinal.ticketyap.events;

public record CarritoConItemsEvent(
    Integer idUsuario,
    String email,
    String nombre,
    int numItems
) {}

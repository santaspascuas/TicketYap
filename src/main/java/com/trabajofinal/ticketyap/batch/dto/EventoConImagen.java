package com.trabajofinal.ticketyap.batch.dto;

import com.trabajofinal.ticketyap.modelos.Eventos;

public record EventoConImagen(Eventos evento, String imageUrl) {}

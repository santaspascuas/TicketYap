package com.trabajofinal.ticketyap.events;

import java.util.List;

public record CompraCompletadaEvent(String email, List<Integer> entradaIds) {}

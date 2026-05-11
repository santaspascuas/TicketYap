package com.trabajofinal.ticketyap.modelos.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CarritoItemDTO {
    private final Integer id_item;
    private final Integer id_carrito;
    private final Integer id_entrada;
    private final BigDecimal precio_venta;
    private final String estado_entrada;
    private final Integer id_evento;
    private final LocalDateTime fecha_agregado;

    public CarritoItemDTO(Integer id_item, Integer id_carrito, Integer id_entrada,
                          BigDecimal precio_venta, String estado_entrada,
                          Integer id_evento, LocalDateTime fecha_agregado) {
        this.id_item = id_item;
        this.id_carrito = id_carrito;
        this.id_entrada = id_entrada;
        this.precio_venta = precio_venta;
        this.estado_entrada = estado_entrada;
        this.id_evento = id_evento;
        this.fecha_agregado = fecha_agregado;
    }

    public Integer getId_item() { return id_item; }
    public Integer getId_carrito() { return id_carrito; }
    public Integer getId_entrada() { return id_entrada; }
    public BigDecimal getPrecio_venta() { return precio_venta; }
    public String getEstado_entrada() { return estado_entrada; }
    public Integer getId_evento() { return id_evento; }
    public LocalDateTime getFecha_agregado() { return fecha_agregado; }
}

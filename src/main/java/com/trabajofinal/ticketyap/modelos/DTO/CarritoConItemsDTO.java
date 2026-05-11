package com.trabajofinal.ticketyap.modelos.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class CarritoConItemsDTO {
    private final Integer id_carrito;
    private final Integer id_usuario;
    private final String estado;
    private final LocalDateTime fecha_modificado;
    private final List<CarritoItemDTO> items;
    private final BigDecimal total;

    public CarritoConItemsDTO(Integer id_carrito, Integer id_usuario, String estado,
                               LocalDateTime fecha_modificado, List<CarritoItemDTO> items) {
        this.id_carrito = id_carrito;
        this.id_usuario = id_usuario;
        this.estado = estado;
        this.fecha_modificado = fecha_modificado;
        this.items = items;
        this.total = items.stream()
            .map(CarritoItemDTO::getPrecio_venta)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Integer getId_carrito() { return id_carrito; }
    public Integer getId_usuario() { return id_usuario; }
    public String getEstado() { return estado; }
    public LocalDateTime getFecha_modificado() { return fecha_modificado; }
    public List<CarritoItemDTO> getItems() { return items; }
    public BigDecimal getTotal() { return total; }
}

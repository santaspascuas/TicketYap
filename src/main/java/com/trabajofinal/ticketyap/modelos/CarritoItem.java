package com.trabajofinal.ticketyap.modelos;

import java.time.LocalDateTime;

public class CarritoItem {
    private Integer id_item;
    private Integer id_carrito;
    private Integer id_entrada;
    private LocalDateTime fecha_agregado;

    public CarritoItem(Integer id_carrito, Integer id_entrada) {
        this.id_carrito = id_carrito;
        this.id_entrada = id_entrada;
    }

    public CarritoItem(Integer id_item, Integer id_carrito, Integer id_entrada, LocalDateTime fecha_agregado) {
        this.id_item = id_item;
        this.id_carrito = id_carrito;
        this.id_entrada = id_entrada;
        this.fecha_agregado = fecha_agregado;
    }

    public CarritoItem() {}

    public Integer getId_item() { return id_item; }
    public void setId_item(Integer id_item) { this.id_item = id_item; }

    public Integer getId_carrito() { return id_carrito; }
    public void setId_carrito(Integer id_carrito) { this.id_carrito = id_carrito; }

    public Integer getId_entrada() { return id_entrada; }
    public void setId_entrada(Integer id_entrada) { this.id_entrada = id_entrada; }

    public LocalDateTime getFecha_agregado() { return fecha_agregado; }
    public void setFecha_agregado(LocalDateTime fecha_agregado) { this.fecha_agregado = fecha_agregado; }
}

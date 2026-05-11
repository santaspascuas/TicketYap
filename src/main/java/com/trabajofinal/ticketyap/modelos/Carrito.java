package com.trabajofinal.ticketyap.modelos;

import java.time.LocalDateTime;

public class Carrito {
    private Integer id_carrito;
    private Integer id_usuario;
    private LocalDateTime fecha_agregado;
    private LocalDateTime fecha_modificado;
    private EstadoCarrito estado;



    public Carrito(Integer id_usuario, LocalDateTime fecha_agregado, LocalDateTime fecha_modificado, EstadoCarrito estado) {
        this.id_usuario = id_usuario;
        this.fecha_agregado = fecha_agregado;
        this.fecha_modificado = fecha_modificado;
        this.estado = estado;
    }

    //Constructor con id_carrito
    public Carrito(Integer id_carrito, Integer id_usuario, LocalDateTime fecha_agregado, LocalDateTime fecha_modificado, EstadoCarrito estado) {
        this.id_carrito = id_carrito;
        this.id_usuario = id_usuario;
        this.fecha_agregado = fecha_agregado;
        this.fecha_modificado = fecha_modificado;
        this.estado = estado;
    }

    public Carrito() {}


    public Integer getId_carrito() {
        return id_carrito;
    }

    public void setId_carrito(Integer id_carrito) {
        this.id_carrito = id_carrito;
    }

    public Integer getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(Integer id_usuario) {
        this.id_usuario = id_usuario;
    }

    public LocalDateTime getFecha_agregado() {
        return fecha_agregado;
    }

    public void setFecha_agregado(LocalDateTime fecha_agregado) {
        this.fecha_agregado = fecha_agregado;
    }

    public LocalDateTime getFecha_modificado() {
        return fecha_modificado;
    }

    public void setFecha_modificado(LocalDateTime fecha_modificado) {
        this.fecha_modificado = fecha_modificado;
    }

    public EstadoCarrito getEstado() {
        return estado;
    }

    public void setEstado(EstadoCarrito estado) {
        this.estado = estado;
    }


    

    








}

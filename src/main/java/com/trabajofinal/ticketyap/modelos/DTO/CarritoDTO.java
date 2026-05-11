package com.trabajofinal.ticketyap.modelos.DTO;

import java.time.LocalDateTime;

public class CarritoDTO {
    private Integer id_carrito;
    private Integer id_usuario;
    private String estado;
    private LocalDateTime fecha_modificado;




    public CarritoDTO(Integer id_carrito, Integer id_usuario, String estado, LocalDateTime fecha_modificado) {
        this.id_carrito = id_carrito;
        this.id_usuario = id_usuario;
        this.estado = estado;
        this.fecha_modificado = fecha_modificado;
    }
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
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public LocalDateTime getFecha_modificado() {
        return fecha_modificado;
    }
    public void setFecha_modificado(LocalDateTime fecha_modificado) {
        this.fecha_modificado = fecha_modificado;
    }



    


    
}

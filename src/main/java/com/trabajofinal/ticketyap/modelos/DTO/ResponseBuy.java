package com.trabajofinal.ticketyap.modelos.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ResponseBuy {
    private Integer id_entrada;
    private BigDecimal precio_venta;
    private LocalDate fecha_publicacion;
    private Integer id_evento;
    private Integer id_usuario;


    public ResponseBuy() {}

    public ResponseBuy(Integer id_entrada, LocalDate fecha_publicacion, Integer id_evento, Integer id_usuario, BigDecimal precio_venta) {
        this.id_entrada = id_entrada;
        this.fecha_publicacion = fecha_publicacion;
        this.id_evento = id_evento;
        this.id_usuario = id_usuario;
        this.precio_venta = precio_venta;
    }

    public Integer getId_entrada() {
        return id_entrada;
    }

    public void setId_entrada(Integer id_entrada) {
        this.id_entrada = id_entrada;
    }

    public BigDecimal getPrecio_venta() {
        return precio_venta;
    }

    public void setPrecio_venta(BigDecimal precio_venta) {
        this.precio_venta = precio_venta;
    }

    public LocalDate getFecha_publicacion() {
        return fecha_publicacion;
    }

    public void setFecha_publicacion(LocalDate fecha_publicacion) {
        this.fecha_publicacion = fecha_publicacion;
    }

    public Integer getId_evento() {
        return id_evento;
    }

    public void setId_evento(Integer id_evento) {
        this.id_evento = id_evento;
    }

    public Integer getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(Integer id_usuario) {
        this.id_usuario = id_usuario;
    }





    


  

}

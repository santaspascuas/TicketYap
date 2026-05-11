package com.trabajofinal.ticketyap.modelos.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;

public class EntradaFront {

    private Integer id_evento;
    private LocalDate fecha_publicacion;
    private BigDecimal precio_venta;
    private Integer id_pdf;
    private String hash;
    

    public EntradaFront(LocalDate fecha_publicacion, Integer id_evento, BigDecimal precio_venta, Integer id_pdf) {
        this.fecha_publicacion = LocalDate.now();
        this.id_evento = id_evento;
        this.precio_venta = precio_venta;
        this.id_pdf = id_pdf;
    }

    public EntradaFront(){}

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Integer getId_evento() {
        return id_evento;
    }

    public void setId_evento(Integer id_evento) {
        this.id_evento = id_evento;
    }

    public Integer getId_pdf(){
        return id_pdf;
    }

    public void setId_pdf(Integer id_pdf){
        this.id_pdf = id_pdf;
    }

    public LocalDate getFecha_publicacion() {
        return fecha_publicacion;
    }

    public void setFecha_publicacion(LocalDate fecha_publicacion) {
        this.fecha_publicacion = fecha_publicacion;
    }

    public BigDecimal getPrecio_venta() {
        return precio_venta;
    }

    public void setPrecio_venta(BigDecimal precio_venta) {
        this.precio_venta = precio_venta;
    }

    @Override
    public String toString() {
        return "EntradaFront [id_evento=" + id_evento + ", fecha_publicacion=" + fecha_publicacion + ", precio_venta="
                + precio_venta + ", id_pdf=" + id_pdf + ", hash=" + hash + "]";
    }







    
}

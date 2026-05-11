package com.trabajofinal.ticketyap.modelos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class Entrada {
    //Iniciamos las entradas.
    private Integer entrada_id;
    private Integer id_evento;
    private Integer id_vendedor;
    private BigDecimal precio_venta;
    private String estado_entrada;
    private LocalDate fecha_publicacion;
    private UUID iduuid;
    private Integer id_pdf;




    public Entrada (Integer id_evento,Integer id_vendedor,BigDecimal precio_venta,
        String estado_entrada, LocalDate fecha_publicacion,Integer id_pdf) {
            this.id_evento = id_evento;
            this.id_vendedor = id_vendedor;
            this.precio_venta = precio_venta;
            this.estado_entrada = estado_entrada;
            this.fecha_publicacion = fecha_publicacion;
            this.iduuid = UUID.randomUUID();
            this.id_pdf = id_pdf;
    }

    public Entrada(){}


    //Consttuctor con id

    public Entrada (Integer entrada_id,Integer id_evento, Integer id_vendedor,BigDecimal precio_venta,String estado_entrada,
        LocalDate fecha_publicacion,UUID iduuid,Integer id_pdf
     ){
        this.entrada_id = entrada_id;
        this.id_evento = id_evento;
        this.id_vendedor = id_vendedor;
        this. precio_venta = precio_venta;
        this.estado_entrada = estado_entrada;
        this.fecha_publicacion = fecha_publicacion;
        this.iduuid = iduuid;
        this.id_pdf = id_pdf;
    }










    public Integer getId_pdf() {
        return id_pdf;
    }

    public void setId_pdf(Integer id_pdf) {
        this.id_pdf = id_pdf;
    }

    public Integer getEntrada_id() {
        return entrada_id;
    }

    public void setEntrada_id(Integer entrada_id) {
        this.entrada_id = entrada_id;
    }
    
        public void setIduuid(UUID iduuid) {
        this.iduuid = iduuid;
    }

    public Integer getId_evento() {
        return id_evento;
    }

    public void setId_evento(Integer id_evento) {
        this.id_evento = id_evento;
    }

    public Integer getId_vendedor() {
        return id_vendedor;
    }

    public void setId_vendedor(Integer id_vendedor) {
        this.id_vendedor = id_vendedor;
    }

    public BigDecimal getPrecio_venta() {
        return precio_venta;
    }

    public void setPrecio_venta(BigDecimal precio_venta) {
        this.precio_venta = precio_venta;
    }

    public String getEstado_entrada() {
        return estado_entrada;
    }

    public void setEstado_entrada(String estado_entrada) {
        this.estado_entrada = estado_entrada;
    }

    public LocalDate getFecha_publicacion() {
        return fecha_publicacion;
    }

    public void setFecha_publicacion(LocalDate fecha_publicacion) {
        this.fecha_publicacion = fecha_publicacion;
    }

    public UUID getIduuid() {
        return iduuid;
    }



}

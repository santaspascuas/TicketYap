package com.trabajofinal.ticketyap.modelos;

import java.time.LocalDate;
import java.util.UUID;

public class Eventos {


    private Integer evento_id;
    private String nombre_evento;
    private Integer tipo_evento_id;
    private Integer localizacion_id;
    private LocalDate fecha_evento;
    private String descripcion;
    private UUID webident;

    public Eventos(Integer evento_id, String nombre_evento,Integer tipo_evento_id, Integer localizacion_id,
    LocalDate fecha_evento, String descripcion) {
        this.evento_id = evento_id;
        this.nombre_evento = nombre_evento;
        this.tipo_evento_id =tipo_evento_id;
        this.localizacion_id = localizacion_id;
        this.fecha_evento = fecha_evento;
        this.descripcion = descripcion;
        this.webident = UUID.randomUUID();
    }

    public Integer getEvento_id() {
        return evento_id;
    }

    public void setEvento_id(Integer evento_id) {
        this.evento_id = evento_id;
    }

    public String getNombre_evento() {
        return nombre_evento;
    }

    public void setNombre_evento(String nombre_evento) {
        this.nombre_evento = nombre_evento;
    }

    public Integer getTipo_evento_id() {
        return tipo_evento_id;
    }

    public void setTipo_evento_id(Integer tipo_evento_id) {
        this.tipo_evento_id = tipo_evento_id;
    }

    public Integer getLocalizacion_id() {
        return localizacion_id;
    }

    public void setLocalizacion_id(Integer localizacion_id) {
        this.localizacion_id = localizacion_id;
    }

    public LocalDate getFecha_evento() {
        return fecha_evento;
    }

    public void setFecha_evento(LocalDate fecha_evento) {
        this.fecha_evento = fecha_evento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public UUID getWebident() {
        return webident;
    }

    public void setWebident(UUID webident) {
        this.webident = webident;
    }


}

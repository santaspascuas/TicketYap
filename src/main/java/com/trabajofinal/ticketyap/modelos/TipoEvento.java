package com.trabajofinal.ticketyap.modelos;

public class TipoEvento {
    private Integer tipo_evento_id;
    private String nombre;


    public TipoEvento(String nombre) {
        this.nombre = nombre;
    }

    public TipoEvento(){}

    public Integer getTipo_evento_id() {
        return tipo_evento_id;
    }

    public void setTipo_evento_id(Integer tipo_evento_id) {
        this.tipo_evento_id = tipo_evento_id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
}

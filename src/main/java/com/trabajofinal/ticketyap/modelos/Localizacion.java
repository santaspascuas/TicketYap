package com.trabajofinal.ticketyap.modelos;

import java.io.Serializable;

public class Localizacion  implements  Serializable{
    
    private Integer localizacion_id;
    private String nombre;
    private String calle;
    private String municipio;
    private String localidad;
    private String codigo_postal;

    public Localizacion(Integer localizacion_id, String nombre, String calle,String municipio, String localidad, String codigo_postal) {
        this.localizacion_id = localizacion_id;
        this.nombre = nombre;
        this.calle = calle;
        this.municipio = municipio;
        this.localidad = localidad;
        this.codigo_postal = codigo_postal;
    }

    public Integer getLocalizacion_id() {
        return localizacion_id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCalle() {
        return calle;
    }

    public String getMunicipio() {
        return municipio;
    }

    public String getLocalidad() {
        return localidad;
    }

    public String getCodigo_postal() {
        return codigo_postal;
    }

    public void setLocalizacion_id(Integer localizacion_id) {
        this.localizacion_id = localizacion_id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public void setCodigo_postal(String codigo_postal) {
        this.codigo_postal = codigo_postal;
    }
}

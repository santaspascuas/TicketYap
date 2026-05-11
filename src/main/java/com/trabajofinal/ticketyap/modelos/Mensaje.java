package com.trabajofinal.ticketyap.modelos;

import java.time.LocalDateTime;

public class Mensaje {

    private Integer id_mensaje;
    private Integer id_conversacion;
    private Integer id_emisor;
    private String contenido;
    private LocalDateTime fecha_envio;
    private boolean leido;

    public Mensaje(Integer id_conversacion, Integer id_emisor, String contenido) {
        this.id_conversacion = id_conversacion;
        this.id_emisor       = id_emisor;
        this.contenido       = contenido;
        this.leido           = false;
    }

    public Mensaje(Integer id_mensaje, Integer id_conversacion, Integer id_emisor,
                   String contenido, LocalDateTime fecha_envio, boolean leido) {
        this.id_mensaje      = id_mensaje;
        this.id_conversacion = id_conversacion;
        this.id_emisor       = id_emisor;
        this.contenido       = contenido;
        this.fecha_envio     = fecha_envio;
        this.leido           = leido;
    }

    public Mensaje() {}

    public Integer getId_mensaje()               { return id_mensaje; }
    public void setId_mensaje(Integer v)         { this.id_mensaje = v; }

    public Integer getId_conversacion()          { return id_conversacion; }
    public void setId_conversacion(Integer v)    { this.id_conversacion = v; }

    public Integer getId_emisor()                { return id_emisor; }
    public void setId_emisor(Integer v)          { this.id_emisor = v; }

    public String getContenido()                 { return contenido; }
    public void setContenido(String v)           { this.contenido = v; }

    public LocalDateTime getFecha_envio()        { return fecha_envio; }
    public void setFecha_envio(LocalDateTime v)  { this.fecha_envio = v; }

    public boolean isLeido()                     { return leido; }
    public void setLeido(boolean v)              { this.leido = v; }
}

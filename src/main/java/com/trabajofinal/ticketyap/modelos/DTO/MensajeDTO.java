package com.trabajofinal.ticketyap.modelos.DTO;

import java.time.LocalDateTime;

public class MensajeDTO {

    private final Integer id;
    private final Integer id_conversacion;
    private final Integer id_emisor;
    private final String nombre_emisor;
    private final String contenido;
    private final LocalDateTime fecha_envio;
    private final boolean leido;
    private final boolean es_propio;

    public MensajeDTO(Integer id, Integer id_conversacion, Integer id_emisor,
                      String nombre_emisor, String contenido,
                      LocalDateTime fecha_envio, boolean leido, boolean es_propio) {
        this.id              = id;
        this.id_conversacion = id_conversacion;
        this.id_emisor       = id_emisor;
        this.nombre_emisor   = nombre_emisor;
        this.contenido       = contenido;
        this.fecha_envio     = fecha_envio;
        this.leido           = leido;
        this.es_propio       = es_propio;
    }

    public Integer getId()                       { return id; }
    public Integer getId_conversacion()          { return id_conversacion; }
    public Integer getId_emisor()                { return id_emisor; }
    public String getNombre_emisor()             { return nombre_emisor; }
    public String getContenido()                 { return contenido; }
    public LocalDateTime getFecha_envio()        { return fecha_envio; }
    public boolean isLeido()                     { return leido; }
    public boolean isEs_propio()                 { return es_propio; }
}

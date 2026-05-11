package com.trabajofinal.ticketyap.modelos.DTO;

import java.time.LocalDateTime;
import java.util.List;

public class ConversacionDTO {

    // 'id' como nombre de campo → Jackson serializa como "id" (lo que espera el frontend)
    private final Integer id;
    private final Integer id_entrada;
    private final Integer id_comprador;
    private final Integer id_vendedor;
    private final String nombre_comprador;
    private final String nombre_vendedor;
    private final LocalDateTime fecha_creacion;
    private final String ultimo_mensaje;
    private final LocalDateTime fecha_ultimo_mensaje;
    private final int mensajes_no_leidos;
    private final List<MensajeDTO> mensajes;

    public ConversacionDTO(Integer id, Integer id_entrada,
                           Integer id_comprador, Integer id_vendedor,
                           String nombre_comprador, String nombre_vendedor,
                           LocalDateTime fecha_creacion,
                           String ultimo_mensaje, LocalDateTime fecha_ultimo_mensaje,
                           int mensajes_no_leidos,
                           List<MensajeDTO> mensajes) {
        this.id                   = id;
        this.id_entrada           = id_entrada;
        this.id_comprador         = id_comprador;
        this.id_vendedor          = id_vendedor;
        this.nombre_comprador     = nombre_comprador;
        this.nombre_vendedor      = nombre_vendedor;
        this.fecha_creacion       = fecha_creacion;
        this.ultimo_mensaje       = ultimo_mensaje;
        this.fecha_ultimo_mensaje = fecha_ultimo_mensaje;
        this.mensajes_no_leidos   = mensajes_no_leidos;
        this.mensajes             = mensajes;
    }

    public Integer getId()                         { return id; }
    public Integer getId_entrada()                 { return id_entrada; }
    public Integer getId_comprador()               { return id_comprador; }
    public Integer getId_vendedor()                { return id_vendedor; }
    public String getNombre_comprador()            { return nombre_comprador; }
    public String getNombre_vendedor()             { return nombre_vendedor; }
    public LocalDateTime getFecha_creacion()       { return fecha_creacion; }
    public String getUltimo_mensaje()              { return ultimo_mensaje; }
    public LocalDateTime getFecha_ultimo_mensaje() { return fecha_ultimo_mensaje; }
    public int getMensajes_no_leidos()             { return mensajes_no_leidos; }
    public List<MensajeDTO> getMensajes()          { return mensajes; }
}

package com.trabajofinal.ticketyap.modelos;

import java.time.LocalDateTime;

public class Conversacion {

    private Integer id_conversacion;
    private Integer id_entrada;
    private Integer id_comprador;
    private Integer id_vendedor;
    private LocalDateTime fecha_creacion;

    public Conversacion(Integer id_entrada, Integer id_comprador, Integer id_vendedor) {
        this.id_entrada = id_entrada;
        this.id_comprador = id_comprador;
        this.id_vendedor = id_vendedor;
    }

    public Conversacion(Integer id_conversacion, Integer id_entrada, Integer id_comprador,
                        Integer id_vendedor, LocalDateTime fecha_creacion) {
        this.id_conversacion = id_conversacion;
        this.id_entrada = id_entrada;
        this.id_comprador = id_comprador;
        this.id_vendedor = id_vendedor;
        this.fecha_creacion = fecha_creacion;
    }

    public Conversacion() {}

    public Integer getId_conversacion()              { return id_conversacion; }
    public void setId_conversacion(Integer v)        { this.id_conversacion = v; }

    public Integer getId_entrada()                   { return id_entrada; }
    public void setId_entrada(Integer v)             { this.id_entrada = v; }

    public Integer getId_comprador()                 { return id_comprador; }
    public void setId_comprador(Integer v)           { this.id_comprador = v; }

    public Integer getId_vendedor()                  { return id_vendedor; }
    public void setId_vendedor(Integer v)            { this.id_vendedor = v; }

    public LocalDateTime getFecha_creacion()         { return fecha_creacion; }
    public void setFecha_creacion(LocalDateTime v)   { this.fecha_creacion = v; }
}

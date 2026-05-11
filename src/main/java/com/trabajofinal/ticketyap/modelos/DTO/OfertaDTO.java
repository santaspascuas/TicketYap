package com.trabajofinal.ticketyap.modelos.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OfertaDTO {

    private final Integer id_oferta;
    private final Integer id_entrada;
    private final Integer id_comprador;
    private final String nombre_comprador;
    private final BigDecimal precio_oferta;
    private final String estado_oferta;
    private final LocalDateTime fecha_oferta;
    private final LocalDateTime fecha_expiracion;
    private final String mensaje;

    public OfertaDTO(Integer id_oferta, Integer id_entrada, Integer id_comprador,
                     String nombre_comprador, BigDecimal precio_oferta,
                     String estado_oferta, LocalDateTime fecha_oferta,
                     LocalDateTime fecha_expiracion, String mensaje) {
        this.id_oferta        = id_oferta;
        this.id_entrada       = id_entrada;
        this.id_comprador     = id_comprador;
        this.nombre_comprador = nombre_comprador;
        this.precio_oferta    = precio_oferta;
        this.estado_oferta    = estado_oferta;
        this.fecha_oferta     = fecha_oferta;
        this.fecha_expiracion = fecha_expiracion;
        this.mensaje          = mensaje;
    }

    public Integer getId_oferta()              { return id_oferta; }
    public Integer getId_entrada()             { return id_entrada; }
    public Integer getId_comprador()           { return id_comprador; }
    public String getNombre_comprador()        { return nombre_comprador; }
    public BigDecimal getPrecio_oferta()       { return precio_oferta; }
    public String getEstado_oferta()           { return estado_oferta; }
    public LocalDateTime getFecha_oferta()     { return fecha_oferta; }
    public LocalDateTime getFecha_expiracion() { return fecha_expiracion; }
    public String getMensaje()                 { return mensaje; }
}

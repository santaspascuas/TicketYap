package com.trabajofinal.ticketyap.modelos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Oferta {

    private Integer id_oferta;
    private Integer id_entrada;
    private Integer id_comprador;
    private BigDecimal precio_oferta;
    private String estado_oferta;
    private LocalDateTime fecha_oferta;
    private LocalDateTime fecha_expiracion;
    private String mensaje;

    public Oferta(Integer id_entrada, Integer id_comprador, BigDecimal precio_oferta, String mensaje) {
        this.id_entrada    = id_entrada;
        this.id_comprador  = id_comprador;
        this.precio_oferta = precio_oferta;
        this.estado_oferta = EstadoOferta.PENDIENTE;
        this.mensaje       = mensaje;
    }

    public Oferta(Integer id_oferta, Integer id_entrada, Integer id_comprador,
                  BigDecimal precio_oferta, String estado_oferta,
                  LocalDateTime fecha_oferta, LocalDateTime fecha_expiracion, String mensaje) {
        this.id_oferta        = id_oferta;
        this.id_entrada       = id_entrada;
        this.id_comprador     = id_comprador;
        this.precio_oferta    = precio_oferta;
        this.estado_oferta    = estado_oferta;
        this.fecha_oferta     = fecha_oferta;
        this.fecha_expiracion = fecha_expiracion;
        this.mensaje          = mensaje;
    }

    public Oferta() {}

    public static final class EstadoOferta {
        public static final String PENDIENTE = "PENDIENTE";
        public static final String ACEPTADA  = "ACEPTADA";
        public static final String RECHAZADA = "RECHAZADA";

        private EstadoOferta() {}
    }

    public Integer getId_oferta()                  { return id_oferta; }
    public void setId_oferta(Integer v)            { this.id_oferta = v; }

    public Integer getId_entrada()                 { return id_entrada; }
    public void setId_entrada(Integer v)           { this.id_entrada = v; }

    public Integer getId_comprador()               { return id_comprador; }
    public void setId_comprador(Integer v)         { this.id_comprador = v; }

    public BigDecimal getPrecio_oferta()           { return precio_oferta; }
    public void setPrecio_oferta(BigDecimal v)     { this.precio_oferta = v; }

    public String getEstado_oferta()               { return estado_oferta; }
    public void setEstado_oferta(String v)         { this.estado_oferta = v; }

    public LocalDateTime getFecha_oferta()         { return fecha_oferta; }
    public void setFecha_oferta(LocalDateTime v)   { this.fecha_oferta = v; }

    public LocalDateTime getFecha_expiracion()     { return fecha_expiracion; }
    public void setFecha_expiracion(LocalDateTime v) { this.fecha_expiracion = v; }

    public String getMensaje()                     { return mensaje; }
    public void setMensaje(String v)               { this.mensaje = v; }
}

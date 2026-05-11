package com.trabajofinal.ticketyap.modelos.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OfertaRequest {

    private Integer id_entrada;
    private BigDecimal precio_oferta;
    private String mensaje;
    private LocalDateTime fecha_expiracion;

    public Integer getId_entrada()                   { return id_entrada; }
    public void setId_entrada(Integer v)             { this.id_entrada = v; }

    public BigDecimal getPrecio_oferta()             { return precio_oferta; }
    public void setPrecio_oferta(BigDecimal v)       { this.precio_oferta = v; }

    public String getMensaje()                       { return mensaje; }
    public void setMensaje(String v)                 { this.mensaje = v; }

    public LocalDateTime getFecha_expiracion()       { return fecha_expiracion; }
    public void setFecha_expiracion(LocalDateTime v) { this.fecha_expiracion = v; }
}

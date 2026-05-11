package com.trabajofinal.ticketyap.modelos.DTO;

import com.trabajofinal.ticketyap.modelos.Entrada;

public class PdfValidacionResultado {

    private boolean valido;
    private String codigoQr;
    private String mensaje;
    private Integer idEntrada;
    private Integer idEvento;
    private String estadoEntrada;

    private PdfValidacionResultado() {}

    public static PdfValidacionResultado valido(String codigoQr, Entrada entrada) {
        PdfValidacionResultado r = new PdfValidacionResultado();
        r.valido        = true;
        r.codigoQr      = codigoQr;
        r.mensaje       = "Entrada válida";
        r.idEntrada     = entrada.getEntrada_id();
        r.idEvento      = entrada.getId_evento();
        r.estadoEntrada = entrada.getEstado_entrada();
        return r;
    }

    public static PdfValidacionResultado invalido(String codigoQr, String razon) {
        PdfValidacionResultado r = new PdfValidacionResultado();
        r.valido    = false;
        r.codigoQr  = codigoQr;
        r.mensaje   = razon;
        return r;
    }

    public boolean isValido()          { return valido; }
    public String getCodigoQr()        { return codigoQr; }
    public String getMensaje()         { return mensaje; }
    public Integer getIdEntrada()      { return idEntrada; }
    public Integer getIdEvento()       { return idEvento; }
    public String getEstadoEntrada()   { return estadoEntrada; }
}

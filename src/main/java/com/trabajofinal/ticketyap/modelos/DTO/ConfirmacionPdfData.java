package com.trabajofinal.ticketyap.modelos.DTO;

public class ConfirmacionPdfData {

    private String evento;
    private String nombre;

    public ConfirmacionPdfData(String evento, String nombre) {
        this.evento = evento;
        this.nombre = nombre;
    }

    public ConfirmacionPdfData() {}

    public String getEvento()        { return evento; }
    public void setEvento(String v)  { this.evento = v; }

    public String getNombre()        { return nombre; }
    public void setNombre(String v)  { this.nombre = v; }
}

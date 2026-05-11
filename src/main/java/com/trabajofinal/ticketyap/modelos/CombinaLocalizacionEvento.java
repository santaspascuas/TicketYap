package com.trabajofinal.ticketyap.modelos;

public class CombinaLocalizacionEvento {

    private Localizacion localizacion;
    private Eventos evento;

    public CombinaLocalizacionEvento(Eventos evento, Localizacion localizacion) {
        this.evento = evento;
        this.localizacion = localizacion;
    }

    public Localizacion getLocalizacion() {
        return localizacion;
    }

    public void setLocalizacion(Localizacion localizacion) {
        this.localizacion = localizacion;
    }

    public Eventos getEvento() {
        return evento;
    }

    public void setEvento(Eventos evento) {
        this.evento = evento;
    }




}

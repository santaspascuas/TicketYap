package com.trabajofinal.ticketyap.modelos.DTO;

import java.time.LocalDate;

public class  ImagenEventoDto {
    private Integer id_entrada;
    private String path_imagen;
    private Integer evento_id;
    private LocalDate fecha_publicacion;
    private String UrlTemporal;

    public ImagenEventoDto(String UrlTemporal, Integer evento_id, LocalDate fecha_publicacion, Integer id_entrada) {
        this.UrlTemporal = UrlTemporal;
        this.evento_id = evento_id;
        this.fecha_publicacion = fecha_publicacion;
        this.id_entrada = id_entrada;
    }

    public Integer getId_entrada() {
        return id_entrada;
    }

    public void setId_entrada(Integer id_entrada) {
        this.id_entrada = id_entrada;
    }

    public String getPath_imagen() {
        return path_imagen;
    }

    public void setPath_imagen(String path_imagen) {
        this.path_imagen = path_imagen;
    }

    public Integer getEvento_id() {
        return evento_id;
    }

    public void setEvento_id(Integer evento_id) {
        this.evento_id = evento_id;
    }

    public LocalDate getFecha_publicacion() {
        return fecha_publicacion;
    }

    public void setFecha_publicacion(LocalDate fecha_publicacion) {
        this.fecha_publicacion = fecha_publicacion;
    }

    public String getUrlTemporal() {
        return UrlTemporal;
    }

    public void setUrlTemporal(String UrlTemporal) {
        this.UrlTemporal = UrlTemporal;
    }






    



    
}

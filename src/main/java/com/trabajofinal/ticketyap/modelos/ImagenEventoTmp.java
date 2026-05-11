package com.trabajofinal.ticketyap.modelos;

import java.time.LocalDate;

public class ImagenEventoTmp {
    private Integer id_entrada;
    private String path_imagen;
    private Integer evento_id;
    private LocalDate fecha_publicacion;

    public ImagenEventoTmp( String path_imagen, Integer evento_id,LocalDate fecha_publicacion) {
        this.fecha_publicacion = LocalDate.now();
        this.evento_id = evento_id;
        this.path_imagen = path_imagen;
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



    @Override
    public String toString() {
        return "ImagenEventoTmp [id_entrada=" + id_entrada + ", path_imagen=" + path_imagen + ", evento_id=" + evento_id
                + ", fecha_publicacion=" + fecha_publicacion + "]";
    }

    

}

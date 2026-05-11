package com.trabajofinal.ticketyap.modelos;

import java.time.LocalDate;

public class Imagentmp {
    private Integer  id_entrada;
    private String path_imagen;
    private Integer id_pdf;
    private LocalDate fecha_publicacion;



    //Le doy constructor

    public Imagentmp(String path_imagen, Integer id_pdf,LocalDate fecha_publicacion  ){
        this.path_imagen = path_imagen;
        this.id_pdf = id_pdf;
        this.fecha_publicacion = LocalDate.now();
    }

    //his.fecha_publicacion = LocalDate.now();

    public Imagentmp() {
        
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
    public Integer getId_pdf() {
        return id_pdf;
    }
    public void setId_pdf(Integer id_pdf) {
        this.id_pdf = id_pdf;
    }
    public LocalDate getFecha_publicacion() {
        return fecha_publicacion;
    }
    public void setFecha_publicacion(LocalDate fecha_publicacion) {
        this.fecha_publicacion = fecha_publicacion;
    }





    


    


}

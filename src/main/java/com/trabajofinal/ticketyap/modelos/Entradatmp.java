package com.trabajofinal.ticketyap.modelos;

import java.time.LocalDate;

public class Entradatmp {

    private Integer id_entrada;
    private String nombre_pdf;
    private Integer id_usuario;
    private Integer num_pagin;
    private String hash;
    private LocalDate fecha_publicacion;


        public Entradatmp(Integer id_entrada, String nombre_pdf, Integer id_usuario, String hash,
        Integer num_pagin,LocalDate fecha_publicacion) {
        this.id_entrada = id_entrada;
        this.nombre_pdf = nombre_pdf;
        this.id_usuario = id_usuario;
        this.num_pagin = num_pagin;
        this.hash = hash;
        this.fecha_publicacion = LocalDate.now();
    }

    // Contructor sin id

    
    public void setId_entrada(Integer id_entrada) {
			this.id_entrada = id_entrada;
		}

		public String getNombre_pdf() {
			return nombre_pdf;
		}

		public void setNombre_pdf(String nombre_pdf) {
			this.nombre_pdf = nombre_pdf;
		}

		public Integer getId_usuario() {
			return id_usuario;
		}

		public void setId_usuario(Integer id_usuario) {
			this.id_usuario = id_usuario;
		}

		public Integer getNum_pagin() {
			return num_pagin;
		}

		public void setNum_pagin(Integer num_pagin) {
			this.num_pagin = num_pagin;
		}

		public String getHash() {
			return hash;
		}

		public void setHash(String hash) {
			this.hash = hash;
		}


		public void setFecha_publicacion(LocalDate fecha_publicacion) {
			this.fecha_publicacion = fecha_publicacion;
		}

	public Integer getId_entrada() {
        return id_entrada;
    }

    
    public Entradatmp(String nombre_pdf, Integer id_usuario, String hash,Integer num_pagin, LocalDate fecha_publicacion) {
        this.nombre_pdf = nombre_pdf;
        this.id_usuario = id_usuario;
        this.hash = hash;
        this.num_pagin = num_pagin;
        this.fecha_publicacion = LocalDate.now();
    }

	public LocalDate getFecha_publicacion() {
		return fecha_publicacion;
	}




}

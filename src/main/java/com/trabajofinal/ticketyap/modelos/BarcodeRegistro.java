package com.trabajofinal.ticketyap.modelos;

import java.time.LocalDateTime;

public class BarcodeRegistro {

    private Integer       id;
    private String        hashBarcode;       
    private String        contenido;         
    private Integer       idEntradaTmp;      
    private Integer       idUsuario;         
    private LocalDateTime fechaRegistro;

    public BarcodeRegistro() {}

    public BarcodeRegistro(String hashBarcode, String contenido,
                           Integer idEntradaTmp, Integer idUsuario) {
        this.hashBarcode   = hashBarcode;
        this.contenido     = contenido;
        this.idEntradaTmp  = idEntradaTmp;
        this.idUsuario     = idUsuario;
        this.fechaRegistro = LocalDateTime.now();
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getHashBarcode() {
		return hashBarcode;
	}

	public void setHashBarcode(String hashBarcode) {
		this.hashBarcode = hashBarcode;
	}

	public String getContenido() {
		return contenido;
	}

	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	public Integer getIdEntradaTmp() {
		return idEntradaTmp;
	}

	public void setIdEntradaTmp(Integer idEntradaTmp) {
		this.idEntradaTmp = idEntradaTmp;
	}

	public Integer getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	public LocalDateTime getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(LocalDateTime fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	@Override
	public String toString() {
		return "BarcodeRegistro [id=" + id + ", hashBarcode=" + hashBarcode + ", contenido=" + contenido
				+ ", idEntradaTmp=" + idEntradaTmp + ", idUsuario=" + idUsuario + ", fechaRegistro=" + fechaRegistro
				+ "]";
	}

    


}

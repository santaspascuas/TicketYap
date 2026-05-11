package com.trabajofinal.ticketyap.modelos.DTO;

import java.util.Map;

public class EmailRequestDto {
	private String destinatario;
	private String TipoEmail;
	Map<String, Object> variables;
	private byte[] adjunto;
    private String nombreAdjunto;
    
	public String getDestinatario() {
		return destinatario;
	}
	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}
	public String getTipoEmail() {
		return TipoEmail;
	}
	public void setTipoEmail(String tipoEmail) {
		TipoEmail = tipoEmail;
	}
	public Map<String, Object> getVariables() {
		return variables;
	}
	public void setVariables(Map<String, Object> variables) {
		this.variables = variables;
	}
	public byte[] getAdjunto() {
		return adjunto;
	}
	public void setAdjunto(byte[] adjunto) {
		this.adjunto = adjunto;
	}
	public String getNombreAdjunto() {
		return nombreAdjunto;
	}
	public void setNombreAdjunto(String nombreAdjunto) {
		this.nombreAdjunto = nombreAdjunto;
	}
	

}

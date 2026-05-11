package com.trabajofinal.ticketyap.modelos.DTO;

import java.util.List;

public class FileResponse {

    private List<String> listacodigos;
    private List<String> urls;
    private String hash;
    /** Token RSA firmado por la plataforma como prueba de verificación. Null si RSA no está configurado. */
    private String tokenVerificacion;
    private ConfirmacionPdfData metadatos;

    public FileResponse(List<String> listacodigos, List<String> urls, String hash,
                        String tokenVerificacion, ConfirmacionPdfData metadatos) {
        this.listacodigos      = listacodigos;
        this.urls              = urls;
        this.hash              = hash;
        this.tokenVerificacion = tokenVerificacion;
        this.metadatos         = metadatos;
    }

    public List<String> getListacodigos() { return listacodigos; }
    public void setListacodigos(List<String> listacodigos) { this.listacodigos = listacodigos; }

    public List<String> getUrls() { return urls; }
    public void setUrls(List<String> urls) { this.urls = urls; }

    public String getHash() { return hash; }
    public void setHash(String hash) { this.hash = hash; }

    public String getTokenVerificacion() { return tokenVerificacion; }
    public void setTokenVerificacion(String tokenVerificacion) { this.tokenVerificacion = tokenVerificacion; }

    public ConfirmacionPdfData getMetadatos() { return metadatos; }
    public void setMetadatos(ConfirmacionPdfData metadatos) { this.metadatos = metadatos; }
}

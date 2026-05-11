package com.trabajofinal.ticketyap.dao;

import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

public interface MinioDao {
    String cargaArchivo(MultipartFile file, Integer idusuario);
    byte [] downloadFile( String objectName); 
    void deleteFile(String bucket, String objectName);
    boolean detectarDuplicados(String filename);
    String calcularHashPdf(InputStream inputStream) throws Exception;
    String cargadeImagenes(byte[]imagenesbyte, Integer idusuario, Integer pagina);
    String generarurl(String objectName);
    String generarurl(String objectName, String bucket);
    String cargaImagenesEvento(byte[]imagenesbyte);
    void confirmarBuccket();

}
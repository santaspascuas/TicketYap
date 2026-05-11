package com.trabajofinal.ticketyap.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.io.Files;
import com.trabajofinal.ticketyap.dao.MinioDao;
import com.trabajofinal.ticketyap.exceptions.DuplicadoBucketException;
import com.trabajofinal.ticketyap.exceptions.HashException;
import com.trabajofinal.ticketyap.exceptions.MinioException;

import io.minio.BucketExistsArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.SetBucketPolicyArgs;
import io.minio.StatObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.http.Method;
import jakarta.annotation.PostConstruct;


@Service
public class MinioService  implements MinioDao{


    private static final Logger log = LoggerFactory.getLogger(MinioService.class);

    private final MinioClient minioClient;
    private final String minioInternalUrl;
    private final String minioPublicUrl;
    private final String minioPublicMediaUrl;
    public static final String BUCKET_NAME = "pdf-temporales";
    public static final String BUCKET_IMAGEN = "imagenes-eventos";


    public MinioService(MinioClient minioClient,
                        @org.springframework.beans.factory.annotation.Value("${minio.url}") String minioInternalUrl,
                        @org.springframework.beans.factory.annotation.Value("${minio.public-url}") String minioPublicUrl,
                        @org.springframework.beans.factory.annotation.Value("${minio.public-media-url}") String minioPublicMediaUrl) {
        this.minioClient = minioClient;
        this.minioInternalUrl = minioInternalUrl;
        this.minioPublicUrl = minioPublicUrl;
        this.minioPublicMediaUrl = minioPublicMediaUrl;
    }

    //Genera el constructor que valida la creación del bucket->Post.
    @PostConstruct
    public void init(){
        try{
            log.info("Inicializamos MinioService");
            confirmarBuccket();
            aplicarPoliticaPublica(BUCKET_IMAGEN);
            log.info("MinioService se ha inicializado correctamente");
        }catch(MinioException e){
            log.error("Error al inicializar MinioService: {}", e.getMessage(), e);
        }
    }

    private void aplicarPoliticaPublica(String bucket) {
        try {
            String policy = """
                {
                  "Version":"2012-10-17",
                  "Statement":[{
                    "Effect":"Allow",
                    "Principal":{"AWS":["*"]},
                    "Action":["s3:GetObject"],
                    "Resource":["arn:aws:s3:::%s/*"]
                  }]
                }
                """.formatted(bucket);
            minioClient.setBucketPolicy(SetBucketPolicyArgs.builder().bucket(bucket).config(policy).build());
            log.info("Política pública aplicada al bucket: {}", bucket);
        } catch (Exception e) {
            log.warn("No se pudo aplicar política pública al bucket {}: {}", bucket, e.getMessage());
        }
    }

    @Override
    public String cargaArchivo(MultipartFile file, Integer idusuario) {
        //Le pasamos el archivo y el id del usuario.
        //El id del usuario lo obtenemos cuando hace la peticin post con la cookie del jwt

        log.info("Recibimos el archivo{} para el usuario{}", file.getOriginalFilename(), idusuario);

        try {
            //Extraigo la extension del archivo
            String extension = Files.getFileExtension(file.getOriginalFilename());
            String hashArchivo = calcularHashPdf(file.getInputStream());
            String objectName = "usuarios/" + idusuario + "/" + extension + "/" + hashArchivo + "." + extension;
            log.debug("Generado objectName: {}", objectName);

            if(detectarDuplicados(objectName)){
                log.warn("Archivo duplicado detectado: {}", objectName);
                throw new DuplicadoBucketException("No se puede subir el pdf, esta ya en el servidor:");
                //TODO EXCEPTION.
            }

            minioClient.putObject(
                PutObjectArgs.builder()
                .bucket(BUCKET_NAME)
                .object(objectName)
                .stream(file.getInputStream(), file.getSize(), -1)
                .contentType(file.getContentType())
                .build()
            );

            log.info("Archivo {} subido correctamente para el usuario {}", objectName, idusuario);
            return  objectName;

        }catch(DuplicadoBucketException e){
            //Tirar excepcion o manejar el error de alguna manera
            log.error("Error al subir archivo: {}", e.getMessage(), e);
            throw e;
        } catch(Exception e){
            log.error("Error al subir archivo: {}", e.getMessage(), e);
            throw new MinioException("Error al subir archivo");
        }

    }

    @Override
    public byte[] downloadFile(String objectName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'downloadFile'");
    }

    @Override
    public void deleteFile(String bucket, String objectName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteFile'");
    }

@Override
public boolean detectarDuplicados(String filename) {
    try{
        minioClient.statObject(
        StatObjectArgs.builder()
            .bucket(BUCKET_NAME)
            .object(filename)
            .build()
        );
        log.debug("El archivo '{}' ya existe en el bucket", filename);
        return true;

    }catch(ErrorResponseException e){
        if ("NoSuchKey".equals(e.errorResponse().code())){
                log.debug("El archivo '{}' NO existe en el bucket", filename);
                return false;
        }
    } catch (Exception e) {
        log.error("Error consultando objeto: {}", e.getMessage(), e);
        throw new MinioException("Error consultando objeto");
    }
    return false;
}

    @Override
    public String calcularHashPdf(InputStream inputStream) throws Exception { 
        try{

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] buffer = new byte[8192];
        int bytesRead;

        while ((bytesRead = inputStream.read(buffer)) !=-1){
            digest.update(buffer, 0, bytesRead);
        }
        byte[] hash = digest.digest();
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
        }catch(IOException e){
            throw new HashException("Error al calcular el hash del archivo PDF: " + e.getMessage());
        }finally{
            try{
                inputStream.close();
            }catch(IOException e){
                System.out.println("Error al cerrar el InputStream: " + e.getMessage());
            }
        }

    }

    @Override
public String cargadeImagenes(byte[] imagenesbyte, Integer idusuario, Integer numeroPagina) {
    //Aqui se cargan las imagenes en minio. //Como es un bucle pues claro que se van metiedo
    

    try {
        String objectName = "usuarios/" + idusuario + "/" + "img" + "/"
                            + "/imagenes/" + System.currentTimeMillis() + "_" + numeroPagina + ".jpg";

        if(detectarDuplicados(objectName)){
                System.out.println("Archivo duplicado detectado de imagenes: " + objectName);
                throw new DuplicadoBucketException("No se puede subir las imagenes, esta ya en el servidor:");
                //TODO EXCEPTION.
            }                    

        minioClient.putObject(
            PutObjectArgs.builder()
                .bucket(BUCKET_NAME)
                .object(objectName)
                .stream(new ByteArrayInputStream(imagenesbyte), imagenesbyte.length, -1)
                .contentType("image/jpeg")
                .build()
        );

        return objectName;

    } catch ( DuplicadoBucketException e) {
        throw e;
    }catch(Exception e){
        throw new MinioException("Error al subir la imagen");
    }
}

@Override
public String generarurl(String objectName){
    return generarurl(objectName, BUCKET_NAME);
}

@Override
public String generarurl(String objectName, String bucket){
    if (BUCKET_IMAGEN.equals(bucket)) {
        return minioPublicMediaUrl + "/" + bucket + "/" + objectName;
    }
    try{
        String url = minioClient.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(bucket)
                .object(objectName)
                .expiry(60 * 60 * 24)
                .build()
        );
        return url.replace(minioInternalUrl, minioPublicUrl);
    }catch(Exception e){
        throw new MinioException("Error al generar URL temporal");
    }
}

    @Override
    public String cargaImagenesEvento(byte[] imagenesbyte) {
        try{
            String objectName = "eventos/img/" + System.currentTimeMillis() + ".jpg";

            //Detectamos si hemos subido ya la imagen para el evento

            if(detectarDuplicados(objectName)){
                System.out.println("Archivo duplicado detectado de imagenes: " + objectName);
                throw new DuplicadoBucketException("No se puede subir las imagenes, esta ya en el servidor:");
            }

            //Insertamos en el bucket.
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(BUCKET_IMAGEN)
                    .object(objectName)
                    .stream(new ByteArrayInputStream(imagenesbyte), imagenesbyte.length, -1)
                    .contentType("image/jpeg")
                    .build());

            return  objectName;

        }catch(DuplicadoBucketException e){
            throw e;
        }catch (Exception e) {
            throw new MinioException("Error al subir imagen del evento");
        }

}

    @Override
    public void confirmarBuccket() {
        if(!isMinioAvailable()){
            log.error("Minio no está disponible. Verifique la conexión y configuración.");
            return;
        }

        try{
           if(!minioClient.bucketExists(BucketExistsArgs.builder().bucket(BUCKET_NAME).build())){
                log.debug("Bucket no exite"+ BUCKET_NAME + "Cramos el bucket.....");
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(BUCKET_NAME).build());
           }
           log.debug("Bucket existe: " + BUCKET_NAME);

        }catch(Exception e){
            log.error("Error creando bucket {}: {}", BUCKET_NAME, e.getMessage(), e);
            throw new MinioException("Error al crear el bucket que no existe.");
        }

        //Bucket de las imagenes

        try{
            if(!minioClient.bucketExists(BucketExistsArgs.builder().bucket(BUCKET_IMAGEN).build())){
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(BUCKET_IMAGEN).build());
            }
            log.debug("Bucket existe: " + BUCKET_NAME);
        }catch(Exception e){
            log.error("Error creando bucket {}: {}", BUCKET_IMAGEN, e.getMessage(), e);
            throw new MinioException("Error al crear el bucket de imagenes que no existe.");
        }
    }


    private boolean isMinioAvailable(){
        try{
            minioClient.listBuckets();
            return true;
        }catch(Exception e){
            return false;
        }
    }


}
package com.trabajofinal.ticketyap.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.trabajofinal.ticketyap.exceptions.GoogleUsuarioException;
import com.trabajofinal.ticketyap.modelos.GoogleUserData;




@Service
public class GoogleAuthService {
    @Value("${google.client-id}")
    private String googleClientId;
    private static final Logger log = LoggerFactory.getLogger(GoogleAuthService.class);

    public GoogleUserData  verificarGoogleToken(String credentiales){
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("[{} Inicio del metodo]", method);
        try{
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.
        Builder(new NetHttpTransport(), new GsonFactory().getDefaultInstance()).
        setAudience(Collections.singletonList(googleClientId)).
        build();
        
        GoogleIdToken idtoken = verifier.verify(credentiales);
        log.debug("GoogleIdToken idtoken--", idtoken);
        if(idtoken != null){
            GoogleIdToken.Payload payload = idtoken.getPayload();
            //Ahora creamos el objeto para devolverlo.
            GoogleUserData user = new GoogleUserData();
            user.setEmail(payload.getEmail());
            user.setName((String) payload.get("name"));
            user.setPicture((String) payload.get("picture"));
            user.setGoogleId(payload.getSubject());   
            log.debug("GoogleUserData user--->",user);    
            return user;
        }

        }catch(IOException | GeneralSecurityException e){
            System.out.println("Error verificando token: " + e.getMessage());
            throw new GoogleUsuarioException("Error a la hora de encontrar el token");
        }
        return null;
        
    }


    



    
}

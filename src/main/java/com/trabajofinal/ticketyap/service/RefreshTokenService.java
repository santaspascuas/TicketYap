package com.trabajofinal.ticketyap.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.trabajofinal.ticketyap.dao.RefreshTokenDao;
import com.trabajofinal.ticketyap.exceptions.RefreshTokenException;
import com.trabajofinal.ticketyap.modelos.RefreshToken;



@Service
public class RefreshTokenService {

    private final RefreshTokenDao resfrestokendao;


    public RefreshTokenService(RefreshTokenDao resfrestokendao) {
        this.resfrestokendao = resfrestokendao;
    }

    //creo el token y lo guardo en la database
    public String crearTokenRefresco(Integer usuario){
        //Generamos el token permanente
        RefreshToken token = new RefreshToken(
            UUID.randomUUID().toString(),
            usuario,
            Instant.now().plus(30, ChronoUnit.DAYS)
        );
        //Una vez creado, empezamos a guardar en la database
        System.out.println("Guardo el token de  termporal" 
        + token.getToken()
        );
        Integer guardarToken = resfrestokendao.guardarToken(token);

        if(guardarToken!=1){
            throw new IllegalStateException("No se ha podido guardar el refresh token en la base de datos");
        }

        return token.getToken();

    }

    //Validamos el acceso del token

    public RefreshToken validarTokenRefresco(String token){
                if(token==null || token.isEmpty()){
            throw new RefreshTokenException("El refresh token no puede ser nulo o vacio");
        }

        System.out.println("Token recibido para validar: " + token);
        RefreshToken refreshtokenconfir = resfrestokendao.encontrarToken(token).orElseThrow(
            () -> new IllegalStateException("El refresh token no se ha encontrado en la base de datos"
        ));
        //Una vez obtenemos el token debemos de confirmar si es valido todavia

        Instant tokenvalido = refreshtokenconfir.getExpires_at();
        Instant ahora = Instant.now();
        if(ahora.isBefore(tokenvalido) && !refreshtokenconfir.getRevoked()){
            return refreshtokenconfir;
    }else{
        //Sino es valido debemos de lanzar una excepcion y eliminar el token de la base de datos
        Integer eliminar = resfrestokendao.eliminarTokenPorUsuario(refreshtokenconfir.getUser_id());
        System.out.println("Refresh token eliminado: "+refreshtokenconfir.getToken()+", filas afectadas: "+eliminar);
        throw new IllegalStateException("El refresh token ha expirado. Debe iniciar sesion de nuevo.");
    }

    }







    



}

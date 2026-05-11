package com.trabajofinal.ticketyap.utilitys;

import java.util.Date;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JWutils {

    //La clase para poder hacer las validaciones.
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 2; // 2 horas
    private static final String SECRET_KEY = "843567893696976453275974432697R634976R738467TR678T34865R6834R8763T478378637664538745673865783678548735687R3";

    private  final java.security.Key key;

    public JWutils() {
        byte[] keyBytes = SECRET_KEY.getBytes();
        this.key = Keys.hmacShaKeyFor(keyBytes);
        //Aqui estas preparando tu clave para poder usarla luego en la construcción del token.

    }

    //Generamos el token a partir de la obtención de los datos de correo y password
    //Para ello usamos la clase userdetails del modulo de springboot
      public String generateToken(UserDetails userDetails){
        return Jwts.builder()
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis()+ EXPIRATION_TIME ))
            .signWith(key,SignatureAlgorithm.HS256)
            .compact();
    }

        //Extraemos el nombre del token usando las claims.La informacion interna compactada

    public String extractUsername(String token){
        return extractClaims(token,Claims::getSubject);
    }

        private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction) {
        return claimsTFunction.apply(
            Jwts
            .parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody()
        );
    }

        //Decimos si el token esta expirado de los siete dias
    private boolean isTokenExpired(String token){
        return extractClaims(token, Claims::getExpiration).before(new Date());
    }

    public boolean isValidToken(String token, UserDetails userDetails) {
        //Comprueba que el token corresponde con el usuario en cuestion.
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }



}

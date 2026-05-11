package com.trabajofinal.ticketyap.dao;

import java.util.Optional;

import com.trabajofinal.ticketyap.modelos.RefreshToken;

public interface RefreshTokenDao {

    Optional<RefreshToken> encontrarToken(String token);
    Integer guardarToken(RefreshToken refreshToken);
    Integer eliminarTokenPorUsuario(Integer userId);

}

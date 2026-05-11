package com.trabajofinal.ticketyap.modelos.implementados;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.trabajofinal.ticketyap.dao.RefreshTokenDao;
import com.trabajofinal.ticketyap.modelos.RefreshToken;
import com.trabajofinal.ticketyap.utilitys.RefreshTokenMapper;

@Repository
public class RefreshTokenImplementado implements  RefreshTokenDao{

    private final JdbcTemplate jdbctemplate;

    public RefreshTokenImplementado(JdbcTemplate jdbctemplate){
        this.jdbctemplate = jdbctemplate;
    }

	@Override
	public Optional<RefreshToken> encontrarToken(String token) {
		String sql ="SELECT * FROM refresh_token WHERE token = ?";
        List<RefreshToken> tokens = jdbctemplate.query(sql, new RefreshTokenMapper(), token);
        return tokens.stream().findFirst();
	}

	@Override
	public Integer guardarToken(RefreshToken refreshToken) {
		        String sql = """
                INSERT INTO refresh_token (id, token, user_id, created_at, expires_at, revoked )
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        return jdbctemplate.update(sql,
            refreshToken.getId(),
            refreshToken.getToken(),
            refreshToken.getUser_id(),
            Timestamp.from(refreshToken.getCreated_at()),
            Timestamp.from(refreshToken.getExpires_at()),
            refreshToken.getRevoked()
        );
	}

	@Override
	public Integer eliminarTokenPorUsuario(Integer userId) {
		String sql = "DELETE FROM refresh_token WHERE user_id = ?";
        return jdbctemplate.update(sql, userId);
	}





}

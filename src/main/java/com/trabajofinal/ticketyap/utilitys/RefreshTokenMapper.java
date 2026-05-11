package com.trabajofinal.ticketyap.utilitys;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;

import com.trabajofinal.ticketyap.modelos.RefreshToken;


public class RefreshTokenMapper implements  RowMapper<RefreshToken>{

	@Override
	@Nullable
	public RefreshToken mapRow(ResultSet resultSet, int arg1) throws SQLException {
		 return new RefreshToken(
            resultSet.getObject("id", java.util.UUID.class),
            resultSet.getString("token"),
            resultSet.getInt("user_id"),
            resultSet.getTimestamp("created_at").toInstant(),
            resultSet.getTimestamp("expires_at").toInstant(),
            resultSet.getBoolean("revoked"));
	}

}

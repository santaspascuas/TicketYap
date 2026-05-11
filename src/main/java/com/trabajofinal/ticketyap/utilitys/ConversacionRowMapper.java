package com.trabajofinal.ticketyap.utilitys;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;

import com.trabajofinal.ticketyap.modelos.Conversacion;

public class ConversacionRowMapper implements RowMapper<Conversacion> {

    @Override
    @Nullable
    public Conversacion mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Conversacion(
            rs.getInt("id"),          // PK de la tabla es 'id'
            rs.getInt("id_entrada"),
            rs.getInt("id_comprador"),
            rs.getInt("id_vendedor"),
            rs.getTimestamp("fecha_creacion").toLocalDateTime()
        );
    }
}

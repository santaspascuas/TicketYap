package com.trabajofinal.ticketyap.utilitys;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;

import com.trabajofinal.ticketyap.modelos.DTO.ConversacionDTO;

// Mapea la query enriquecida de selectResumenPorUsuario (sin lista de mensajes)
public class ConversacionDTORowMapper implements RowMapper<ConversacionDTO> {

    @Override
    @Nullable
    public ConversacionDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        Timestamp fechaUltimo = rs.getTimestamp("fecha_ultimo_mensaje");

        return new ConversacionDTO(
            rs.getInt("id"),
            rs.getInt("id_entrada"),
            rs.getInt("id_comprador"),
            rs.getInt("id_vendedor"),
            rs.getString("nombre_comprador"),
            rs.getString("nombre_vendedor"),
            rs.getTimestamp("fecha_creacion").toLocalDateTime(),
            rs.getString("ultimo_mensaje"),
            fechaUltimo != null ? fechaUltimo.toLocalDateTime() : null,
            rs.getInt("mensajes_no_leidos"),
            null   // mensajes no se cargan en la vista de lista
        );
    }
}

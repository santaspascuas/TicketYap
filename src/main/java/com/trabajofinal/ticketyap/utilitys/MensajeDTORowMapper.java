package com.trabajofinal.ticketyap.utilitys;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;

import com.trabajofinal.ticketyap.modelos.DTO.MensajeDTO;

public class MensajeDTORowMapper implements RowMapper<MensajeDTO> {

    private final Integer idUsuarioActual;

    // idUsuarioActual puede ser null cuando se llama sin contexto de usuario
    public MensajeDTORowMapper(Integer idUsuarioActual) {
        this.idUsuarioActual = idUsuarioActual;
    }

    @Override
    @Nullable
    public MensajeDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        int idEmisor = rs.getInt("id_emisor");
        boolean esPropio = idUsuarioActual != null && idUsuarioActual.equals(idEmisor);

        return new MensajeDTO(
            rs.getInt("id"),
            rs.getInt("id_conversacion"),
            idEmisor,
            rs.getString("nombre_emisor"),
            rs.getString("contenido"),
            rs.getTimestamp("fecha_envio").toLocalDateTime(),
            rs.getBoolean("leido"),
            esPropio
        );
    }
}

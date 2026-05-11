package com.trabajofinal.ticketyap.utilitys;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;

import com.trabajofinal.ticketyap.modelos.DTO.OfertaDTO;

public class OfertaDTORowMapper implements RowMapper<OfertaDTO> {

    @Override
    @Nullable
    public OfertaDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        Timestamp expiracion = rs.getTimestamp("fecha_expiracion");
        return new OfertaDTO(
            rs.getInt("id_oferta"),
            rs.getInt("id_entrada"),
            rs.getInt("id_comprador"),
            rs.getString("nombre_comprador"),
            rs.getBigDecimal("precio_oferta"),
            rs.getString("estado_oferta"),
            rs.getTimestamp("fecha_oferta").toLocalDateTime(),
            expiracion != null ? expiracion.toLocalDateTime() : null,
            rs.getString("mensaje")
        );
    }
}

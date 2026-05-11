package com.trabajofinal.ticketyap.utilitys;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.RowMapper;

import com.trabajofinal.ticketyap.modelos.DTO.CarritoItemDTO;

public class CarritoItemDTORowMapper implements RowMapper<CarritoItemDTO> {

    @Override
    public CarritoItemDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        Timestamp ts = rs.getTimestamp("fecha_agregado");
        return new CarritoItemDTO(
            rs.getInt("id_item"),
            rs.getInt("id_carrito"),
            rs.getInt("id_entrada"),
            rs.getBigDecimal("precio_venta"),
            rs.getString("estado_entrada"),
            rs.getInt("id_evento"),
            ts != null ? ts.toLocalDateTime() : null
        );
    }
}

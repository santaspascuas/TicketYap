package com.trabajofinal.ticketyap.utilitys;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.RowMapper;

import com.trabajofinal.ticketyap.modelos.CarritoItem;

public class CarritoItemRowMapper implements RowMapper<CarritoItem> {

    @Override
    public CarritoItem mapRow(ResultSet rs, int rowNum) throws SQLException {
        Timestamp ts = rs.getTimestamp("fecha_agregado");
        return new CarritoItem(
            rs.getInt("id_item"),
            rs.getInt("id_carrito"),
            rs.getInt("id_entrada"),
            ts != null ? ts.toLocalDateTime() : null
        );
    }
}

package com.trabajofinal.ticketyap.utilitys;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;

import com.trabajofinal.ticketyap.modelos.Entrada;

public class EntradaRowMapper implements RowMapper<Entrada> {

    @Override
    @Nullable
    public Entrada mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        
        return new Entrada(
        resultSet.getInt("id_entrada"),
        resultSet.getInt("id_evento"), 
        resultSet.getInt("id_vendedor"), 
        resultSet.getBigDecimal("precio_venta"), 
        resultSet.getString("estado_entrada"), 
        resultSet.getDate("fecha_publicacion").toLocalDate(), 
        resultSet.getObject("iduuid", java.util.UUID.class), 
        resultSet.getInt("id_pdf"));
    }

}

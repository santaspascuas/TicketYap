package com.trabajofinal.ticketyap.utilitys;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.trabajofinal.ticketyap.modelos.Carrito;
import com.trabajofinal.ticketyap.modelos.EstadoCarrito;

public class CarritoRowMapper implements RowMapper<Carrito> {

    @Override
    public Carrito mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new Carrito(
            resultSet.getInt("id_carrito"),
            resultSet.getInt("id_usuario"),
            resultSet.getTimestamp("fecha_agregado").toLocalDateTime(),
            resultSet.getTimestamp("fecha_modificado").toLocalDateTime(),
            EstadoCarrito.valueOf(resultSet.getString("estado"))
        );
    }



    }  

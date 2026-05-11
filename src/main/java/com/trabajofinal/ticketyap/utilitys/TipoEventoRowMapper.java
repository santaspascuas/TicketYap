package com.trabajofinal.ticketyap.utilitys;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.trabajofinal.ticketyap.modelos.TipoEvento;

public class TipoEventoRowMapper implements RowMapper<TipoEvento>{

    @Override
    public TipoEvento mapRow(ResultSet resultSet, int rowNum) throws SQLException {
         TipoEvento nuevo = new TipoEvento();
         nuevo.setTipo_evento_id(resultSet.getInt("tipo_evento_id"));
         nuevo.setNombre(resultSet.getString("nombre"));
         return nuevo;
    }
    

}

package com.trabajofinal.ticketyap.utilitys;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.trabajofinal.ticketyap.modelos.Localizacion;

public class LocalizacionRowMapper  implements  RowMapper<Localizacion>{

    @Override
    public Localizacion mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        
        return new Localizacion(
            resultSet.getInt("localizacion_id"),
            resultSet.getString("nombre"),
            resultSet.getString("calle"),
            resultSet.getString("municipio"),
            resultSet.getString("localidad"),
            resultSet.getString("codigo_postal"));

    }

    

}

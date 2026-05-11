package com.trabajofinal.ticketyap.utilitys;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.trabajofinal.ticketyap.modelos.Eventos;

public class EventosRowMapper  implements RowMapper<Eventos>{

    @Override
    public Eventos mapRow(ResultSet resultsets, int rowNum) throws SQLException {
        Eventos ev = new Eventos(
            resultsets.getInt("evento_id"),
            resultsets.getString("nombre_evento"),
            resultsets.getInt("tipo_evento_id"),
            resultsets.getInt("localizacion_id"),
            resultsets.getDate("fecha_evento").toLocalDate(),
            resultsets.getString("descripcion")
        );
        try {
            String uuidStr = resultsets.getString("webident");
            if (uuidStr != null) {
                ev.setWebident(java.util.UUID.fromString(uuidStr));
            }
        } catch (SQLException ignored) {
            // columna no presente en queries parciales (ej. combinaEvento sin webident)
        }
        return ev;
    }

}

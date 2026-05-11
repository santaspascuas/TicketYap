package com.trabajofinal.ticketyap.utilitys;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.trabajofinal.ticketyap.modelos.ImagenEventoTmp;

public class EventoImagenRowMapper implements RowMapper<ImagenEventoTmp> {

    @Override
    public ImagenEventoTmp mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ImagenEventoTmp(
            rs.getString("path_imagen"),
            rs.getInt("evento_id"),
            rs.getDate("fecha_publicacion").toLocalDate());
    }
    

}

package com.trabajofinal.ticketyap.utilitys;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.trabajofinal.ticketyap.modelos.Imagentmp;

public class ImagenTmpRowMapper implements RowMapper<Imagentmp>{

    @Override
    public Imagentmp mapRow(ResultSet rs, int rowNum) throws SQLException {

        return new Imagentmp(
            rs.getString("path_imagen"),
            rs.getInt("id_pdf"),
            rs.getDate("fecha_publicacion").toLocalDate());
        
    }

}

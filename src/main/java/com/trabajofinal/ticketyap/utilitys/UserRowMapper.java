package com.trabajofinal.ticketyap.utilitys;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.trabajofinal.ticketyap.modelos.User;

public class UserRowMapper implements RowMapper<User>{

    @Override
    public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        
        return new User(
        resultSet.getInt("id_usuario"),
        resultSet.getString("nombre"),
        resultSet.getString("email"),
        resultSet.getString("contrasena"),
        resultSet.getString("numero_cuenta"),
        resultSet.getDate("fecha_registro").toLocalDate(),
        resultSet.getString("role"));
    }


}

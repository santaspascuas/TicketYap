package com.trabajofinal.ticketyap.utilitys;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;

import com.trabajofinal.ticketyap.modelos.Entradatmp;

public class EntradatmpRowMapper implements RowMapper<Entradatmp>{

	@Override
	@Nullable
	public Entradatmp mapRow(ResultSet resultSet, int arg1) throws SQLException {
		// TODO Auto-generated method stub
		return new Entradatmp(
            resultSet.getInt("id_entrada"),
            resultSet.getString("nombre_pdf"),
            resultSet.getInt("id_usuario"),
            resultSet.getString("hash"),
            resultSet.getInt("num_pagin"),
            resultSet.getDate("fecha_publicacion").toLocalDate());
	}

}

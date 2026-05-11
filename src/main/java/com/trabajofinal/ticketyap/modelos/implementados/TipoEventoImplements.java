package com.trabajofinal.ticketyap.modelos.implementados;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.trabajofinal.ticketyap.dao.TipoEventoDao;
import com.trabajofinal.ticketyap.modelos.TipoEvento;
import com.trabajofinal.ticketyap.utilitys.TipoEventoRowMapper;


@Repository
public class TipoEventoImplements  implements TipoEventoDao{

    //Uso del jdbcTemplate

    private final JdbcTemplate jdbctemplate;


    public TipoEventoImplements(JdbcTemplate jdbctemplate){
        this.jdbctemplate = jdbctemplate;
    }

    @Override
    public Integer insertarTipoEvento(TipoEvento tipo) {
        String sql = """
                INSERT INTO tipo_evento (nombre) VALUES (?)
                """;
         return jdbctemplate.update(sql, tipo.getNombre());
    }

    @Override
    public List<TipoEvento> listarTipoEvento() {
        String sql = "SELECT * FROM tipo_evento";
        return jdbctemplate.query(sql, new TipoEventoRowMapper());
    }

    @Override
    public Integer deleteTipoEvento(Integer id) {
        String sql = "DELETE FROM tipo_evento WHERE tipo_evento_id = ?";
        return jdbctemplate.update(sql,id);
    }

    @Override
    public Boolean ConfirmaEvento(String nombre) {
        String sql = "SELECT EXISTS(SELECT 1 FROM tipo_evento WHERE nombre = ?)";
        return jdbctemplate.queryForObject(sql, Boolean.class, nombre);
    }



}

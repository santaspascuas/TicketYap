package com.trabajofinal.ticketyap.modelos.implementados;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.trabajofinal.ticketyap.dao.LocalizacionDao;
import com.trabajofinal.ticketyap.modelos.Localizacion;
import com.trabajofinal.ticketyap.utilitys.LocalizacionRowMapper;


@Repository
public class LocalizacionImplements  implements  LocalizacionDao{

    private final JdbcTemplate jdbctemplate;

    public LocalizacionImplements(JdbcTemplate jdbctemplate){
        this.jdbctemplate = jdbctemplate;
    }

    @Override
    public List<Localizacion> selectLocalizacion() {
        String sql = "SELECT * FROM localizacion";
        return jdbctemplate.query(sql, new LocalizacionRowMapper());
    }

    @Override
    public Integer insertarLocalizacion(Localizacion local) {

        String sql = """
        INSERT INTO localizacion (nombre, calle, municipio, localidad, codigo_postal)
        VALUES (?,?,?,?,?)
                """;

        return jdbctemplate.update(sql,
        local.getNombre(),
        local.getCalle(),
        local.getMunicipio(),
        local.getLocalidad(),
        local.getCodigo_postal());        
    }

    @Override
    public Integer deleteLocalizacion(Integer localizacion) {
        String sql = "DELETE FROM localizacion WHERE localizacion_id  = ?";
        return jdbctemplate.update(sql,localizacion);

    }

    @Override
    public Integer UpdateLocalizacion(Localizacion local) {
        String sql = """
                UPDATE localizacion SET nombre = ?, calle = ?, municipio = ?, localidad = ?, codigo_postal = ?
                WHERE localizacion_id =?
                """;
                
        return jdbctemplate.update(sql,
          local.getNombre(),
            local.getCalle(),
            local.getMunicipio(),
            local.getLocalidad(),
            local.getCodigo_postal(),
            local.getLocalizacion_id()); 
    }

    @Override
    public Optional<Localizacion> buscarlocalizacion(Integer localizacion) {
        String sql = "SELECT * FROM localizacion WHERE localizacion_id = ?";
        try {
             Localizacion buscado = jdbctemplate.queryForObject(sql, new LocalizacionRowMapper(), localizacion);
             return Optional.of(buscado);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Localizacion> buscarpornombre(String localnombre) {
        String sql = "SELECT * FROM localizacion WHERE nombre = ?";
        List<Localizacion> buscado= jdbctemplate.query(sql, new LocalizacionRowMapper(), localnombre);
        return buscado.stream().findFirst();
    }

}

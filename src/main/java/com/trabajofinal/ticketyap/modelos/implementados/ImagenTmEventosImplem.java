package com.trabajofinal.ticketyap.modelos.implementados;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.trabajofinal.ticketyap.dao.ImagenEventoTmpDao;
import com.trabajofinal.ticketyap.modelos.ImagenEventoTmp;
import com.trabajofinal.ticketyap.utilitys.EventoImagenRowMapper;

@Repository
public class ImagenTmEventosImplem  implements ImagenEventoTmpDao{

    private final JdbcTemplate jdbctemplate;

    public ImagenTmEventosImplem(JdbcTemplate jdbctemplate) {
        this.jdbctemplate = jdbctemplate;
    }

    @Override
    public void guardarImagenEventotmp(ImagenEventoTmp imagenevento) {

        String sql = """
        INSERT INTO evento_imagen (path_imagen,evento_id,fecha_publicacion) 
        VALUES (?,?,?)       
                """;
        jdbctemplate.update(sql,
            imagenevento.getPath_imagen(),
            imagenevento.getEvento_id(),
            imagenevento.getFecha_publicacion()
        );
    }

    @Override
    public boolean eliminarImagenEventotmp(Integer id_imagen) {
        String sql = "DELETE FROM evento_imagen WHERE id_evento_imagen = ?";
        return jdbctemplate.update(sql, id_imagen)>0;
    }

    @Override
    public List<ImagenEventoTmp> obtenerImagenporID(Integer id_evento) {
        String sql="SELECT * FROM evento_imagen WHERE evento_id = ?";
        return jdbctemplate.query(sql, new EventoImagenRowMapper(), id_evento);
    }

    @Override
    public Optional<ImagenEventoTmp> buscarImagenporidPrincipal(String path_name) {
        String sql = "SELECT * FROM evento_imagen WHERE path_imagen = ?";

        try{
            ImagenEventoTmp encontrado =  jdbctemplate.queryForObject(sql, new EventoImagenRowMapper(), path_name);
            return Optional.ofNullable(encontrado);
        }catch(DataAccessException e){
            return Optional.empty();
        }
    }

    @Override
    public List<ImagenEventoTmp> obtenerAllImagenEvento() {
         String sql= "Select * from  evento_imagen";
         return jdbctemplate.query(sql,new EventoImagenRowMapper());
    }
    

}

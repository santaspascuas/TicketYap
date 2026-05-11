package com.trabajofinal.ticketyap.modelos.implementados;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.trabajofinal.ticketyap.dao.ImagenTmpDao;
import com.trabajofinal.ticketyap.modelos.Imagentmp;
import com.trabajofinal.ticketyap.utilitys.ImagenTmpRowMapper;

@Repository
public class ImagenImplemenstmp  implements ImagenTmpDao{

    //Uso jdbc perro
    private final JdbcTemplate jdbctemplate;

    public ImagenImplemenstmp(JdbcTemplate jdbctemplate){
        this.jdbctemplate = jdbctemplate;
    }


    @Override
    public void guardarImagenTmp(Imagentmp imagentmp) {
        String sql = """
        INSERT INTO entrada_pdf_imagen (path_imagen,id_pdf,fecha_publicacion) 
        VALUES (?,?,?)""";

        jdbctemplate.update(sql,
            imagentmp.getPath_imagen(),
            imagentmp.getId_pdf(),
            imagentmp.getFecha_publicacion());
    }

    @Override
    public boolean eliminarImagenTmp(Integer id_pdf) {
        String sql = "DELETE FROM entrada_pdf_imagen WHERE id_pdf = ?";
        return jdbctemplate.update(sql, id_pdf) > 0;
    }


    @Override
    public List<Imagentmp> obtenerImagenTmpPorId(Integer id_pdf) {
        String sql="SELECT * FROM entrada_pdf_imagen WHERE id_pdf = ?";
        return jdbctemplate.query(sql, new ImagenTmpRowMapper(), id_pdf);
    }

}

package com.trabajofinal.ticketyap.modelos.implementados;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.trabajofinal.ticketyap.dao.EntradatmpDao;
import com.trabajofinal.ticketyap.modelos.Entradatmp;
import com.trabajofinal.ticketyap.utilitys.EntradatmpRowMapper;

@Repository
public class EntradatmpImplements  implements  EntradatmpDao {

    private final JdbcTemplate jdbctemplate;

    public EntradatmpImplements(JdbcTemplate jdbctemplate){
        this.jdbctemplate = jdbctemplate;
    }

    @Override
    public Integer insertartmpfile(Entradatmp entradatmp) {
        	   String sql = """
                INSERT INTO entrada_pdf_tmp (nombre_pdf,id_usuario,num_pagin,hash,fecha_publicacion)
                VALUES (?, ?, ?, ?,?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        //El problema esta en que se extrae todas las keys de las colmunas y solo quiero el id

        jdbctemplate.update(connection -> { 
            PreparedStatement ps = connection.prepareStatement(sql, 
            new String[] { "id_entrada" }); 
            ps.setString(1, entradatmp.getNombre_pdf()); 
            ps.setInt(2, entradatmp.getId_usuario());
            ps.setInt(3, entradatmp.getNum_pagin()); 
            ps.setString(4, entradatmp.getHash()); 
            ps.setDate(5, java.sql.Date.valueOf(entradatmp.getFecha_publicacion())); 
            return ps; }, 
            keyHolder);
        return keyHolder.getKey().intValue();
    }

    @Override
    public Optional<Entradatmp> obtenerEntradatmp(String nombreentrada) {
        String sql = "SELECT * FROM entrada_pdf_tmp WHERE nombre_pdf = ?";
        List<Entradatmp> obtenerEntrada = jdbctemplate.query(sql, new EntradatmpRowMapper(), nombreentrada);
        return obtenerEntrada.stream().findFirst();
    }

    @Override
    public Integer deleteEntradatmp(Integer id_entrada) {
        String sql  = "DELETE FROM entrada_pdf_tmp WHERE id_entrada = ?";
        return jdbctemplate.update(sql,id_entrada);
    }

    @Override
    public boolean buscaEntradatmpbyhash(String hash) {
        String sql = "SELECT COUNT(*) FROM entrada_pdf_tmp WHERE hash = ?";
        Integer count = jdbctemplate.queryForObject(sql, Integer.class, hash);
        return count != null && count > 0;
    }

    @Override
    public Optional<Entradatmp> obtenerEntradabyHash(String hash) {
        String sql =" SELECT * FROM entrada_pdf_tmp WHERE hash = ?";

        try{
            Entradatmp entrada = jdbctemplate.queryForObject(sql, new EntradatmpRowMapper(), hash);
            return Optional.ofNullable(entrada);
        }catch(EmptyResultDataAccessException e){
            return Optional.empty();
        }

    }

}

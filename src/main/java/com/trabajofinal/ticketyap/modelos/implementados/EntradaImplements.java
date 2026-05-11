package com.trabajofinal.ticketyap.modelos.implementados;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.trabajofinal.ticketyap.dao.EntradaDao;
import com.trabajofinal.ticketyap.modelos.Entrada;
import com.trabajofinal.ticketyap.utilitys.EntradaRowMapper;

@Repository
public class EntradaImplements  implements EntradaDao{

     private final JdbcTemplate jdbctemplate; 


     public EntradaImplements(JdbcTemplate jdbctemplate){
        this.jdbctemplate = jdbctemplate;
     }

    @Override
    public List<Entrada> selectEntradas() {
        String sql = "SELECT * FROM entrada";
        return jdbctemplate.query(sql, new EntradaRowMapper());
    }

    @Override
    public Optional<Entrada> selectEntradabyuser(Integer iduser) {
        String sql = "SELECT * FROM entrada WHERE id_vendedor = ?";
        try {
            Entrada nueva = jdbctemplate.queryForObject(sql, new EntradaRowMapper(), iduser);
            return Optional.of(nueva);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Integer insertarEntrada(Entrada entrada) {
        String sql = """
                 INSERT INTO entrada (id_evento, id_vendedor,precio_venta, estado_entrada,fecha_publicacion, iduuid, id_pdf)
        VALUES (?, ?, ?, ?, ?, ?, ?)
                """;
        return jdbctemplate.update(sql,
            entrada.getId_evento(),
            entrada.getId_vendedor(),
            entrada.getPrecio_venta(),
            entrada.getEstado_entrada(),
            entrada.getFecha_publicacion(),
            entrada.getIduuid(),
            entrada.getId_pdf()
        );
    }

    @Override
    public Integer deleteEntrada(Integer user, Integer entrada) {
        String sql = "DELETE FROM entrada WHERE id_vendedor = ? AND id_entrada = ?";
        return jdbctemplate.update(sql, user, entrada);
    }

    @Override
    public Integer updateEntrada(Entrada update) {
        String sql = """
                UPDATE entrada
                SET id_evento = ?, id_vendedor = ?, precio_venta = ?, estado_entrada = ?, fecha_publicacion = ?
                WHERE id_entrada = ?
                """;
        return jdbctemplate.update(sql,
            update.getId_evento(),
            update.getId_vendedor(),
            update.getPrecio_venta(),
            update.getEstado_entrada(),
            update.getFecha_publicacion(),
            update.getId_pdf()
        );
    }

  
    @Override
    public Optional<Entrada> selectEntradaById(Integer idEntrada) {
        String sql = "SELECT * FROM entrada WHERE id_entrada = ?";
        try {
            Entrada entrada = jdbctemplate.queryForObject(sql, new EntradaRowMapper(), idEntrada);
            return Optional.of(entrada);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Entrada> selectEntradabyqr(String qr) {
        String sql = "SELECT * FROM entrada WHERE iduuid::text = ?";
        try {
            Entrada entrada = jdbctemplate.queryForObject(sql, new EntradaRowMapper(), qr);
            return Optional.of(entrada);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Entrada>selectEntradadisponiblebyid(Integer idevento) {
        String sql = "SELECT * FROM entrada WHERE id_evento = ? AND LOWER(estado_entrada) = 'disponible'";
        return jdbctemplate.query(sql, new EntradaRowMapper(), idevento);
    }

    @Override
    public List<Entrada> selectEntradasPropias(Integer userId) {
        String sql = "SELECT * FROM entrada WHERE id_vendedor = ? AND estado_entrada != 'Comprado'";
        return jdbctemplate.query(sql, new EntradaRowMapper(), userId);
    }

    @Override
    public List<Entrada> selectEntradasCompradas(Integer userId) {
        String sql = "SELECT * FROM entrada WHERE id_vendedor = ? AND estado_entrada = 'Comprado'";
        return jdbctemplate.query(sql, new EntradaRowMapper(), userId);
    }

    @Override
    public Integer updateEstadoCompra(Integer idEntrada, Integer iduser, String estado) {
    String sql = """
        UPDATE entrada 
        SET id_vendedor = ?, estado_entrada = ?
        WHERE id_entrada = ?
        """;

    return jdbctemplate.update(sql,
        iduser, estado, idEntrada
    );
}
}

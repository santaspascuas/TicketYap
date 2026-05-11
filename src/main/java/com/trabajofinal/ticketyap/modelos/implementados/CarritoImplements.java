package com.trabajofinal.ticketyap.modelos.implementados;

import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.trabajofinal.ticketyap.dao.CarritoDao;
import com.trabajofinal.ticketyap.modelos.Carrito;
import com.trabajofinal.ticketyap.utilitys.CarritoRowMapper;

@Repository
public class CarritoImplements implements CarritoDao{

    private final JdbcTemplate jdbctemplate;

    public CarritoImplements(JdbcTemplate jdbctemplate) {
        this.jdbctemplate = jdbctemplate;
    }

    @Override
    public Optional<Carrito> obtenerCarritoActivoByUser(Integer id_usuario) {
        String sql = "SELECT * FROM carrito WHERE id_usuario =? AND estado = 'ACTIVO'";
        try{

            Carrito carrito = jdbctemplate.queryForObject(sql, new CarritoRowMapper(), id_usuario);
            return Optional.of(carrito);
        }catch(Exception e){
            return Optional.empty();
        }
    }

    @Override
    public Integer eliminarCarrito(Integer id_carrito) {
        String sql = "DELETE FROM carrito WHERE id_carrito = ?";
        return jdbctemplate.update(sql, id_carrito);
    }

    @Override
    public Integer crearCarrito(Carrito carrito) {
        String sql ="""
        INSERT INTO carrito (id_usuario, fecha_agregado, fecha_modificado, estado)
        VALUES (?, ?, ?, ?)
                """;
        return jdbctemplate.update(sql,
            carrito.getId_usuario(),
            carrito.getFecha_agregado(),
            carrito.getFecha_modificado(),
            carrito.getEstado().name());
    }

    @Override
    public Integer actualizarCarrito(Carrito carrito) {
        String sql = """
            UPDATE carrito 
            SET fecha_modificado = ?, estado = ? 
            WHERE id_carrito = ?
            """;
        return jdbctemplate.update(sql,
            carrito.getFecha_modificado(),
            carrito.getEstado().name(),
            carrito.getId_carrito());
    }

    @Override
    public Optional<Carrito> obtenerCarritoById(Integer id_carrito) {
        String sql = "SELECT * FROM carrito WHERE id_carrito = ?";
        try {
            Carrito carrito = jdbctemplate.queryForObject(sql, new CarritoRowMapper(), id_carrito);
            return Optional.of(carrito);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Integer actualizarEstadoCarrito(Integer id_carrito, String estado) {
        String sql = """
            UPDATE carrito 
            SET estado = ? 
            WHERE id_carrito = ?
            """;
        return jdbctemplate.update(sql, estado, id_carrito);
    }
    
    @Override
    public Integer actualizarFechaModificacion(Integer id_carrito) {
        String sql = """
            UPDATE carrito 
            SET fecha_modificado = ? 
            WHERE id_carrito = ?
            """;
        return jdbctemplate.update(sql, new java.util.Date(), id_carrito);
    }




    
}

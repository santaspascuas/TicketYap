package com.trabajofinal.ticketyap.modelos.implementados;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.trabajofinal.ticketyap.dao.CarritoItemDao;
import com.trabajofinal.ticketyap.modelos.CarritoItem;
import com.trabajofinal.ticketyap.modelos.DTO.CarritoItemDTO;
import com.trabajofinal.ticketyap.utilitys.CarritoItemDTORowMapper;
import com.trabajofinal.ticketyap.utilitys.CarritoItemRowMapper;

@Component
public class CarritoItemImplements implements CarritoItemDao {

    private final JdbcTemplate jdbc;

    public CarritoItemImplements(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Integer agregarItem(CarritoItem item) {
        String sql = """
            INSERT INTO carrito_item (id_carrito, id_entrada)
            VALUES (?, ?)
            """;
        return jdbc.update(sql, item.getId_carrito(), item.getId_entrada());
    }

    @Override
    public Integer eliminarItem(Integer id_item) {
        String sql = "DELETE FROM carrito_item WHERE id_item = ?";
        return jdbc.update(sql, id_item);
    }

    @Override
    public List<CarritoItemDTO> obtenerItemsPorCarrito(Integer id_carrito) {
        String sql = """
            SELECT ci.id_item, ci.id_carrito, ci.id_entrada, ci.fecha_agregado,
                   e.precio_venta, e.estado_entrada, e.id_evento
            FROM carrito_item ci
            JOIN entrada e ON ci.id_entrada = e.id_entrada
            WHERE ci.id_carrito = ?
            ORDER BY ci.fecha_agregado DESC
            """;
        return jdbc.query(sql, new CarritoItemDTORowMapper(), id_carrito);
    }

    @Override
    public Integer vaciarCarrito(Integer id_carrito) {
        String sql = "DELETE FROM carrito_item WHERE id_carrito = ?";
        return jdbc.update(sql, id_carrito);
    }

    @Override
    public boolean existeEntradaEnCarrito(Integer id_carrito, Integer id_entrada) {
        String sql = """
            SELECT COUNT(*) FROM carrito_item
            WHERE id_carrito = ? AND id_entrada = ?
            """;
        Integer count = jdbc.queryForObject(sql, Integer.class, id_carrito, id_entrada);
        return count != null && count > 0;
    }

    @Override
    public Optional<CarritoItem> obtenerItemById(Integer id_item) {
        String sql = "SELECT * FROM carrito_item WHERE id_item = ?";
        try {
            CarritoItem item = jdbc.queryForObject(sql, new CarritoItemRowMapper(), id_item);
            return Optional.of(item);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }
}

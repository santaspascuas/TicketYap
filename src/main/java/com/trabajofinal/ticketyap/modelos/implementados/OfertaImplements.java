package com.trabajofinal.ticketyap.modelos.implementados;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.trabajofinal.ticketyap.dao.OfertaDao;
import com.trabajofinal.ticketyap.modelos.Oferta;
import com.trabajofinal.ticketyap.modelos.DTO.OfertaDTO;
import com.trabajofinal.ticketyap.utilitys.OfertaDTORowMapper;

@Component
public class OfertaImplements implements OfertaDao {

    private final JdbcTemplate jdbc;

    public OfertaImplements(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Integer insertar(Oferta oferta) {
        String sql = """
            INSERT INTO oferta (id_entrada, id_comprador, precio_oferta, estado_oferta, fecha_expiracion, mensaje)
            VALUES (?, ?, ?, ?, ?, ?)
            """;
        return jdbc.update(sql,
            oferta.getId_entrada(),
            oferta.getId_comprador(),
            oferta.getPrecio_oferta(),
            oferta.getEstado_oferta(),
            oferta.getFecha_expiracion(),
            oferta.getMensaje());
    }

    @Override
    public Optional<OfertaDTO> selectById(Integer idOferta) {
        String sql = """
            SELECT o.*, u.nombre AS nombre_comprador
            FROM oferta o
            JOIN usuario u ON o.id_comprador = u.id_usuario
            WHERE o.id_oferta = ?
            """;
        return jdbc.query(sql, new OfertaDTORowMapper(), idOferta)
            .stream().findFirst();
    }

    @Override
    public List<OfertaDTO> selectPorEntrada(Integer idEntrada) {
        String sql = """
            SELECT o.*, u.nombre AS nombre_comprador
            FROM oferta o
            JOIN usuario u ON o.id_comprador = u.id_usuario
            WHERE o.id_entrada = ?
            ORDER BY o.fecha_oferta DESC
            """;
        return jdbc.query(sql, new OfertaDTORowMapper(), idEntrada);
    }

    @Override
    public List<OfertaDTO> selectPorComprador(Integer idComprador) {
        String sql = """
            SELECT o.*, u.nombre AS nombre_comprador
            FROM oferta o
            JOIN usuario u ON o.id_comprador = u.id_usuario
            WHERE o.id_comprador = ?
            ORDER BY o.fecha_oferta DESC
            """;
        return jdbc.query(sql, new OfertaDTORowMapper(), idComprador);
    }

    @Override
    public Integer actualizarEstado(Integer idOferta, String estado) {
        String sql = "UPDATE oferta SET estado_oferta = ? WHERE id_oferta = ?";
        return jdbc.update(sql, estado, idOferta);
    }

    @Override
    public Integer rechazarPendientesPorEntrada(Integer idEntrada) {
        String sql = """
            UPDATE oferta SET estado_oferta = 'RECHAZADA'
            WHERE id_entrada = ? AND estado_oferta = 'PENDIENTE'
            """;
        return jdbc.update(sql, idEntrada);
    }

    @Override
    public boolean existeOfertaPendiente(Integer idEntrada, Integer idComprador) {
        String sql = """
            SELECT COUNT(*) FROM oferta
            WHERE id_entrada = ? AND id_comprador = ? AND estado_oferta = 'PENDIENTE'
            """;
        Integer count = jdbc.queryForObject(sql, Integer.class, idEntrada, idComprador);
        return count != null && count > 0;
    }
}

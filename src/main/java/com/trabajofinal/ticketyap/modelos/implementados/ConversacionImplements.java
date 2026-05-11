package com.trabajofinal.ticketyap.modelos.implementados;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.trabajofinal.ticketyap.dao.ConversacionDao;
import com.trabajofinal.ticketyap.modelos.Conversacion;
import com.trabajofinal.ticketyap.modelos.Mensaje;
import com.trabajofinal.ticketyap.modelos.DTO.ConversacionDTO;
import com.trabajofinal.ticketyap.modelos.DTO.MensajeDTO;
import com.trabajofinal.ticketyap.utilitys.ConversacionDTORowMapper;
import com.trabajofinal.ticketyap.utilitys.ConversacionRowMapper;
import com.trabajofinal.ticketyap.utilitys.MensajeDTORowMapper;

@Component
public class ConversacionImplements implements ConversacionDao {

    private final JdbcTemplate jdbc;

    public ConversacionImplements(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Integer insertar(Conversacion conversacion) {
        String sql = """
            INSERT INTO conversacion (id_entrada, id_comprador, id_vendedor)
            VALUES (?, ?, ?)
            """;
        return jdbc.update(sql,
            conversacion.getId_entrada(),
            conversacion.getId_comprador(),
            conversacion.getId_vendedor());
    }

    @Override
    public Optional<Conversacion> selectById(Integer idConversacion) {
        String sql = "SELECT * FROM conversacion WHERE id = ?";
        return jdbc.query(sql, new ConversacionRowMapper(), idConversacion)
            .stream().findFirst();
    }

    @Override
    public Optional<Conversacion> selectPorEntradaYComprador(Integer idEntrada, Integer idComprador) {
        String sql = """
            SELECT * FROM conversacion
            WHERE id_entrada = ? AND id_comprador = ?
            """;
        return jdbc.query(sql, new ConversacionRowMapper(), idEntrada, idComprador)
            .stream().findFirst();
    }

    @Override
    public List<Conversacion> selectPorUsuario(Integer idUsuario) {
        String sql = """
            SELECT * FROM conversacion
            WHERE id_comprador = ? OR id_vendedor = ?
            ORDER BY fecha_creacion DESC
            """;
        return jdbc.query(sql, new ConversacionRowMapper(), idUsuario, idUsuario);
    }

    @Override
    public List<ConversacionDTO> selectResumenPorUsuario(Integer idUsuario) {
        String sql = """
            SELECT c.id,
                   c.id_entrada,
                   c.id_comprador,
                   c.id_vendedor,
                   c.fecha_creacion,
                   uc.nombre AS nombre_comprador,
                   uv.nombre AS nombre_vendedor,
                   (SELECT contenido FROM mensaje
                    WHERE id_conversacion = c.id
                    ORDER BY fecha_envio DESC LIMIT 1)   AS ultimo_mensaje,
                   (SELECT fecha_envio FROM mensaje
                    WHERE id_conversacion = c.id
                    ORDER BY fecha_envio DESC LIMIT 1)   AS fecha_ultimo_mensaje,
                   (SELECT COUNT(*) FROM mensaje
                    WHERE id_conversacion = c.id
                      AND id_emisor != ?
                      AND leido = false)                  AS mensajes_no_leidos
            FROM conversacion c
            JOIN usuario uc ON c.id_comprador = uc.id_usuario
            JOIN usuario uv ON c.id_vendedor  = uv.id_usuario
            WHERE c.id_comprador = ? OR c.id_vendedor = ?
            ORDER BY fecha_ultimo_mensaje DESC NULLS LAST
            """;
        return jdbc.query(sql, new ConversacionDTORowMapper(), idUsuario, idUsuario, idUsuario);
    }

    @Override
    public Integer insertarMensaje(Mensaje mensaje) {
        String sql = """
            INSERT INTO mensaje (id_conversacion, id_emisor, contenido)
            VALUES (?, ?, ?)
            """;
        return jdbc.update(sql,
            mensaje.getId_conversacion(),
            mensaje.getId_emisor(),
            mensaje.getContenido());
    }

    @Override
    public List<MensajeDTO> selectMensajes(Integer idConversacion, Integer idUsuario) {
        String sql = """
            SELECT m.id, m.id_conversacion, m.id_emisor,
                   m.contenido, m.fecha_envio, m.leido,
                   u.nombre AS nombre_emisor
            FROM mensaje m
            JOIN usuario u ON m.id_emisor = u.id_usuario
            WHERE m.id_conversacion = ?
            ORDER BY m.fecha_envio ASC
            """;
        return jdbc.query(sql, new MensajeDTORowMapper(idUsuario), idConversacion);
    }

    @Override
    public Optional<MensajeDTO> selectUltimoMensaje(Integer idConversacion) {
        String sql = """
            SELECT m.id, m.id_conversacion, m.id_emisor,
                   m.contenido, m.fecha_envio, m.leido,
                   u.nombre AS nombre_emisor
            FROM mensaje m
            JOIN usuario u ON m.id_emisor = u.id_usuario
            WHERE m.id_conversacion = ?
            ORDER BY m.fecha_envio DESC
            LIMIT 1
            """;
        // es_propio no es relevante para el último mensaje, se pasa null
        return jdbc.query(sql, new MensajeDTORowMapper(null), idConversacion)
            .stream().findFirst();
    }
}

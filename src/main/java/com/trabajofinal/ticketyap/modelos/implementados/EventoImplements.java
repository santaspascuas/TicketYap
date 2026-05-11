package com.trabajofinal.ticketyap.modelos.implementados;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.trabajofinal.ticketyap.dao.EventoDao;
import com.trabajofinal.ticketyap.modelos.CombinaLocalizacionEvento;
import com.trabajofinal.ticketyap.modelos.Eventos;
import com.trabajofinal.ticketyap.utilitys.CombinacionEventoLocaRowMapper;
import com.trabajofinal.ticketyap.utilitys.EventosRowMapper;

@Repository
public class EventoImplements implements EventoDao {

    //Sabemos que la implementación supone que usemos JDBC para realizar las operaciones SQL.

    private final JdbcTemplate jdbctemplate;


    //Inyectamos la dependencia

    public EventoImplements(JdbcTemplate jdbctemplate){
        this.jdbctemplate = jdbctemplate;
    }


    @Override
    public List<Eventos> selectEventos() {
        // Listado de eventos
        String sql = "SELECT * FROM eventos";
        return  jdbctemplate.query(sql, new EventosRowMapper());
    }

    @Override
    public Optional<Eventos> selectEventoByid(Integer id) {
        // Listado de ebentos por id
        String sql = "SELECT * FROM eventos WHERE evento_id = ?";
        /*
         * Como es un unico resultado usarmos queryobjet.
         */
        /*
         * Si el optional no encuentra nada pues devolvera un empty o vacio para ni ser nulo.
         * Curioso porque salta la ecepcion de la base de datos pero en la que tengo personalizada no porque el flujo se ve interrumpido.
         * Usamos try catch para atraparla aqui y luego en la implementacion salta con mi mensaje personalizado.
         */
        try {
            Eventos evento = jdbctemplate.queryForObject(sql, new EventosRowMapper(), id);
            return Optional.of(evento);
        } catch (DataAccessException e) {
            return Optional.empty();
        }

    }

    @Override
    public Integer deleteEvento(Integer id) {
        String sql = "DELETE FROM eventos WHERE evento_id = ?";
        return jdbctemplate.update(sql, id);
    }

    @Override
    public Integer insertarEvento(Eventos eventoInsertado) {
        String sql = """
        
        INSERT INTO eventos (nombre_evento, tipo_evento_id, localizacion_id, fecha_evento, descripcion, webIdent)
        VALUES (?, ?, ?, ?, ?, ?)
                """;

        return jdbctemplate.update(sql,
        eventoInsertado.getNombre_evento(),
        eventoInsertado.getTipo_evento_id(),
        eventoInsertado.getLocalizacion_id(),
        eventoInsertado.getFecha_evento(),
        eventoInsertado.getDescripcion(),
        eventoInsertado.getWebident());
    }

    @Override
    public Optional<Eventos> selectEventoByname(String nombre) {
        String sql = "SELECT * FROM eventos WHERE nombre_evento = ?";
        try {
            Eventos eventodevuelto = jdbctemplate.queryForObject(sql, new EventosRowMapper(), nombre);
            return Optional.of(eventodevuelto);   
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<CombinaLocalizacionEvento> combinaEvento(Integer id) {
        String sql = """
        SELECT
            e.evento_id, e.nombre_evento, e.tipo_evento_id, e.localizacion_id,
            e.fecha_evento, e.descripcion, e.webident,
            l.localizacion_id, l.nombre, l.calle, l.municipio, l.localidad, l.codigo_postal
        FROM eventos e
        INNER JOIN localizacion l ON e.localizacion_id = l.localizacion_id
        WHERE e.evento_id = ?;
                """;
        return jdbctemplate.query(sql, new CombinacionEventoLocaRowMapper(),id);
    }

    @Override
    public Integer updateEvento(Eventos eventoInsertado) {
        
        String sql = """
                UPDATE eventos 
                SET nombre_evento = ?, tipo_evento_id = ?, localizacion_id = ?, fecha_evento = ?, descripcion = ?
                WHERE evento_id = ?
                """;
        return jdbctemplate.update(sql,
            eventoInsertado.getNombre_evento(),
            eventoInsertado.getTipo_evento_id(),
            eventoInsertado.getLocalizacion_id(),
            eventoInsertado.getFecha_evento(),
            eventoInsertado.getDescripcion(),
            eventoInsertado.getEvento_id());
    }

    @Override
    public Optional<Eventos> selectEventoByUUID(UUID uuid) {
        String sql =" SELECT * FROM eventos WHERE webident = ?";
        try {
            Eventos eventoDevuelto = jdbctemplate.queryForObject(sql, new EventosRowMapper(), uuid);
            return Optional.of(eventoDevuelto);
        }catch(DataAccessException e){
            return Optional.empty();
        }

    }

    @Override
    public BigDecimal obtenerPrecioEntrada(Integer id) {
        // Insertamos aqui las querys.
         String sql = """
                 SELECT COALESCE(SUM(precio_venta), 0) AS ingresos_mes
                    FROM entrada
                    WHERE estado_entrada = 'VENDIDA'
                    AND fecha_publicacion >= DATE_TRUNC('month', CURRENT_DATE)
                 """;
        return  jdbctemplate.queryForObject(sql, BigDecimal.class);
    }

    

    




}

package com.trabajofinal.ticketyap.batch.writer;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestClient;

import com.trabajofinal.ticketyap.batch.dto.EventoConImagen;
import com.trabajofinal.ticketyap.modelos.Eventos;
import com.trabajofinal.ticketyap.service.MinioService;

public class EventoItemWriter implements ItemWriter<EventoConImagen> {

    private static final Logger log = LoggerFactory.getLogger(EventoItemWriter.class);

    private final JdbcTemplate jdbc;
    private final MinioService minioService;
    private final RestClient restClient;

    public EventoItemWriter(JdbcTemplate jdbc, MinioService minioService) {
        this.jdbc = jdbc;
        this.minioService = minioService;
        this.restClient = RestClient.create();
        log.info("[Writer] EventoItemWriter inicializado");
    }

    @Override
    public void write(Chunk<? extends EventoConImagen> chunk) {
        log.info("[Writer] ── Inicio chunk — {} evento(s) a escribir ──", chunk.size());

        int insertados = 0;
        int conImagen  = 0;
        int sinImagen  = 0;

        for (EventoConImagen item : chunk) {
            Eventos e = item.evento();

            log.debug("[Writer] Insertando evento en BD:" +
                      " nombre='{}' | tipo_evento_id={} | localizacion_id={} | fecha={} | webident={} | descripcion='{}'",
                      e.getNombre_evento(),
                      e.getTipo_evento_id(),
                      e.getLocalizacion_id(),
                      e.getFecha_evento(),
                      e.getWebident(),
                      e.getDescripcion());

            Integer eventoId = jdbc.queryForObject(
                "INSERT INTO eventos (nombre_evento, tipo_evento_id, localizacion_id, fecha_evento, descripcion, webident) " +
                "VALUES (?, ?, ?, ?, ?, ?) RETURNING evento_id",
                Integer.class,
                e.getNombre_evento(), e.getTipo_evento_id(), e.getLocalizacion_id(),
                e.getFecha_evento(), e.getDescripcion(), e.getWebident());

            log.info("[Writer] Evento insertado — evento_id={} nombre='{}'", eventoId, e.getNombre_evento());
            insertados++;

            if (item.imageUrl() != null && eventoId != null) {
                log.debug("[Writer] Procesando imagen para evento_id={}: {}", eventoId, item.imageUrl());
                subirImagen(item.imageUrl(), eventoId);
                conImagen++;
            } else {
                log.debug("[Writer] Evento_id={} sin URL de imagen — omitiendo subida", eventoId);
                sinImagen++;
            }
        }

        log.info("[Writer] ── Fin chunk — insertados={}, con imagen={}, sin imagen={} ──",
                 insertados, conImagen, sinImagen);
    }

    private void subirImagen(String imageUrl, Integer eventoId) {
        log.debug("[Writer] Descargando imagen para evento_id={}: {}", eventoId, imageUrl);
        try {
            byte[] imageBytes = restClient.get()
                .uri(imageUrl)
                .retrieve()
                .body(byte[].class);

            if (imageBytes == null || imageBytes.length == 0) {
                log.warn("[Writer] Imagen descargada vacía para evento_id={} url='{}'", eventoId, imageUrl);
                return;
            }

            log.debug("[Writer] Imagen descargada — evento_id={}, tamaño={} bytes", eventoId, imageBytes.length);

            String minioPath = minioService.cargaImagenesEvento(imageBytes);
            log.debug("[Writer] Imagen subida a MinIO — path='{}'", minioPath);

            jdbc.update(
                "INSERT INTO evento_imagen (path_imagen, evento_id, fecha_publicacion) VALUES (?, ?, ?)",
                minioPath, eventoId, LocalDate.now());

            log.info("[Writer] evento_imagen insertado — evento_id={} path='{}'", eventoId, minioPath);

        } catch (Exception ex) {
            log.warn("[Writer] No se pudo procesar imagen para evento_id={} url='{}': {} — {}",
                     eventoId, imageUrl, ex.getClass().getSimpleName(), ex.getMessage());
        }
    }
}

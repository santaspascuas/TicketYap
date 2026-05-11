package com.trabajofinal.ticketyap.batch.processor;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.jdbc.core.JdbcTemplate;

import com.trabajofinal.ticketyap.batch.dto.EventoConImagen;
import com.trabajofinal.ticketyap.batch.dto.TicketmasterResponse;
import com.trabajofinal.ticketyap.modelos.Eventos;

public class EventoItemProcessor implements ItemProcessor<TicketmasterResponse.Evento, EventoConImagen> {

    private static final Logger log = LoggerFactory.getLogger(EventoItemProcessor.class);

    private final JdbcTemplate jdbc;
    private final int tipoEventoId;

    /** Registra los eventos ya procesados en este batch (nombre normalizado → item).
     *  Actúa como deduplicador en memoria para detectar repetidos dentro del mismo
     *  lote, antes de que lleguen a escribirse en BD. */
    private final Map<String, EventoConImagen> eventosEnBatch = new HashMap<>();

    public EventoItemProcessor(JdbcTemplate jdbc, int tipoEventoId) {
        this.jdbc = jdbc;
        this.tipoEventoId = tipoEventoId;
        log.info("[Processor] EventoItemProcessor inicializado — tipoEventoId={}", tipoEventoId);
    }

    @Override
    public EventoConImagen process(TicketmasterResponse.Evento item) {
        log.debug("[Processor] ── process() recibido: nombre='{}' ──", item.name());

        // Validar campos obligatorios
        if (item.name() == null || item.name().isBlank()) {
            log.warn("[Processor] Evento descartado — nombre nulo o vacío");
            return null;
        }
        if (item.dates() == null || item.dates().start() == null || item.dates().start().localDate() == null) {
            log.warn("[Processor] Evento descartado — fecha ausente (nombre='{}')", item.name());
            return null;
        }

        // Normalizar nombre: trim + colapsar espacios internos múltiples
        String nombre = item.name().trim().replaceAll("\\s+", " ");
        LocalDate fecha = LocalDate.parse(item.dates().start().localDate());
        log.debug("[Processor] Nombre normalizado='{}', fecha='{}'", nombre, fecha);

        // ── 1. Deduplicación en lote ────────────────────────────────────────
        // Comprobamos el map antes de consultar BD: si el mismo nombre ya apareció
        // en este batch, lo descartamos sin hacer ninguna query adicional.
        if (eventosEnBatch.containsKey(nombre)) {
            log.warn("[Processor] Duplicado en lote, omitiendo: '{}'", nombre);
            return null;
        }

        // ── 2. Deduplicación en BD ──────────────────────────────────────────
        // TRIM en el lado de BD para cubrir registros ya guardados con espacios.
        Integer count = jdbc.queryForObject(
            "SELECT COUNT(*) FROM eventos WHERE TRIM(nombre_evento) = ?",
            Integer.class, nombre);
        if (count != null && count > 0) {
            log.debug("[Processor] Evento ya existe en BD, omitiendo: '{}'", nombre);
            // Registramos en el map para evitar re-consultar BD si vuelve a aparecer
            eventosEnBatch.put(nombre, null);
            return null;
        }
        log.debug("[Processor] '{}' no existe en BD ni en lote — continuando", nombre);

        // Resolver localización
        Integer localizacionId = resolverLocalizacion(item);

        // Seleccionar imagen
        String imageUrl = seleccionarMejorImagen(item.images());
        if (imageUrl != null) {
            log.debug("[Processor] Imagen seleccionada para '{}': {}", nombre, imageUrl);
        } else {
            log.warn("[Processor] Sin imagen disponible para '{}'", nombre);
        }

        // Construir objeto Eventos
        Eventos evento = new Eventos(null, nombre, tipoEventoId, localizacionId, fecha, "Importado de Ticketmaster");

        log.info("[Processor] Evento preparado para inserción:" +
                 " nombre='{}' | fecha={} | tipo_evento_id={} | localizacion_id={} | webident={} | imagen={}",
                 evento.getNombre_evento(),
                 evento.getFecha_evento(),
                 evento.getTipo_evento_id(),
                 evento.getLocalizacion_id(),
                 evento.getWebident(),
                 imageUrl != null ? "sí" : "no");

        // Registrar en el map de este lote para bloquear cualquier repetición posterior
        EventoConImagen resultado = new EventoConImagen(evento, imageUrl);
        eventosEnBatch.put(nombre, resultado);
        log.debug("[Processor] Registrado en map de lote — total únicos hasta ahora: {}", eventosEnBatch.size());

        return resultado;
    }

    private String seleccionarMejorImagen(List<TicketmasterResponse.Imagen> imagenes) {
        if (imagenes == null || imagenes.isEmpty()) {
            log.debug("[Processor] Lista de imágenes vacía o nula");
            return null;
        }

        log.debug("[Processor] Evaluando {} imagen(es) disponibles", imagenes.size());
        if (log.isDebugEnabled()) {
            for (TicketmasterResponse.Imagen img : imagenes) {
                log.debug("[Processor]   url='{}' ratio='{}' size={}x{} fallback={}",
                          img.url(), img.ratio(), img.width(), img.height(), img.fallback());
            }
        }

        String seleccionada = imagenes.stream()
            .filter(img -> !img.fallback() && "16_9".equals(img.ratio()))
            .max(Comparator.comparingInt(TicketmasterResponse.Imagen::width))
            .or(() -> imagenes.stream().filter(img -> !img.fallback()).findFirst())
            .map(TicketmasterResponse.Imagen::url)
            .orElse(null);

        if (seleccionada != null) {
            log.debug("[Processor] Imagen elegida (16:9 mayor width o primera sin fallback): {}", seleccionada);
        } else {
            log.warn("[Processor] No se encontró imagen válida (todas son fallback)");
        }
        return seleccionada;
    }

    private Integer resolverLocalizacion(TicketmasterResponse.Evento item) {
        String nombreVenue = "Sin especificar";
        String ciudad      = "Sin especificar";
        String calle       = "";
        String postalCode  = "";

        if (item.embedded() != null && item.embedded().venues() != null
                && !item.embedded().venues().isEmpty()) {
            TicketmasterResponse.Venue venue = item.embedded().venues().get(0);
            if (venue.name()       != null) nombreVenue = venue.name();
            if (venue.city()       != null && venue.city().name() != null) ciudad = venue.city().name();
            if (venue.address()    != null && venue.address().line1() != null) calle = venue.address().line1();
            if (venue.postalCode() != null) postalCode = venue.postalCode();
        } else {
            log.warn("[Processor] Evento '{}' sin venues — usando localización por defecto", item.name());
        }

        log.debug("[Processor] Venue extraído — nombre='{}', ciudad='{}', calle='{}', cp='{}'",
                  nombreVenue, ciudad, calle, postalCode);

        // Buscar localización existente
        List<Map<String, Object>> rows = jdbc.queryForList(
            "SELECT localizacion_id FROM localizacion WHERE nombre = ? AND municipio = ?",
            nombreVenue, ciudad);

        if (!rows.isEmpty()) {
            Integer locId = ((Number) rows.get(0).get("localizacion_id")).intValue();
            log.debug("[Processor] Localización existente encontrada — id={} para venue='{}' ciudad='{}'",
                      locId, nombreVenue, ciudad);
            return locId;
        }

        // Insertar nueva localización
        log.debug("[Processor] Localización no encontrada — insertando nueva: nombre='{}', calle='{}', municipio='{}', cp='{}'",
                  nombreVenue, calle, ciudad, postalCode);
        Integer newLocId = jdbc.queryForObject(
            "INSERT INTO localizacion (nombre, calle, municipio, localidad, codigo_postal) VALUES (?, ?, ?, ?, ?) RETURNING localizacion_id",
            Integer.class, nombreVenue, calle, ciudad, ciudad, postalCode);
        log.info("[Processor] Nueva localización insertada — id={} nombre='{}' ciudad='{}'",
                 newLocId, nombreVenue, ciudad);
        return newLocId;
    }
}

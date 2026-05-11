package com.trabajofinal.ticketyap.batch.reader;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.web.client.RestClient;

import com.trabajofinal.ticketyap.batch.dto.TicketmasterResponse;

public class TicketmasterItemReader implements ItemReader<TicketmasterResponse.Evento> {

    private static final Logger log = LoggerFactory.getLogger(TicketmasterItemReader.class);
    private static final String BASE_URL = "https://app.ticketmaster.com/discovery/v2/events.json";
    private static final int PAGE_SIZE = 50;
    private static final int MAX_PAGES = 5;

    private final String apiKey;
    private final RestClient restClient;
    private final List<TicketmasterResponse.Evento> buffer = new ArrayList<>();
    private int currentPage = 0;
    private int totalPages = 1;
    private int totalEventosLeidos = 0;

    public TicketmasterItemReader(String apiKey) {
        this.apiKey = apiKey;
        this.restClient = RestClient.create();
        log.info("[Reader] TicketmasterItemReader inicializado — apiKey={}..., pageSize={}, maxPages={}",
                 apiKey.substring(0, Math.min(6, apiKey.length())), PAGE_SIZE, MAX_PAGES);
    }

    @Override
    public TicketmasterResponse.Evento read() {
        if (!buffer.isEmpty()) {
            TicketmasterResponse.Evento next = buffer.remove(0);
            log.debug("[Reader] Entregando evento del buffer (quedan {}): '{}'",
                      buffer.size(), next.name());
            return next;
        }

        if (currentPage >= totalPages || currentPage >= MAX_PAGES) {
            log.info("[Reader] Lectura completada — páginas leídas: {}/{}, total eventos entregados: {}",
                     currentPage, Math.min(totalPages, MAX_PAGES), totalEventosLeidos);
            return null;
        }

        log.debug("[Reader] Buffer vacío, cargando página {}...", currentPage + 1);
        fetchPage(currentPage++);

        if (buffer.isEmpty()) {
            log.warn("[Reader] Buffer sigue vacío tras fetchPage — finalizando lectura");
            return null;
        }

        TicketmasterResponse.Evento next = buffer.remove(0);
        totalEventosLeidos++;
        log.debug("[Reader] Entregando evento (quedan {} en buffer): '{}'", buffer.size(), next.name());
        return next;
    }

    private void fetchPage(int page) {
        log.info("[Reader] ── Fetching página {} (páginas totales conocidas: {}) ──", page + 1, totalPages);
        try {
            TicketmasterResponse response = restClient.get()
                .uri(BASE_URL + "?apikey={key}&countryCode=ES&size={size}&page={page}&sort=date,asc",
                    apiKey, PAGE_SIZE, page)
                .retrieve()
                .body(TicketmasterResponse.class);

            if (response == null) {
                log.warn("[Reader] Respuesta nula de Ticketmaster en página {}", page + 1);
                return;
            }

            if (response.page() != null) {
                totalPages = response.page().totalPages();
                log.debug("[Reader] Paginación — página actual={}, totalPages={}, totalElements aprox={}",
                          response.page().number() + 1, totalPages, totalPages * PAGE_SIZE);
            }

            if (response.embedded() == null || response.embedded().events() == null) {
                log.warn("[Reader] Respuesta sin eventos en página {} (embedded={})",
                         page + 1, response.embedded() == null ? "null" : "presente pero events=null");
                return;
            }

            List<TicketmasterResponse.Evento> events = response.embedded().events();
            buffer.addAll(events);
            totalEventosLeidos += events.size();

            log.info("[Reader] Página {}/{} recibida — {} eventos en esta página, {} en buffer total",
                     page + 1, Math.min(totalPages, MAX_PAGES), events.size(), buffer.size());

            if (log.isDebugEnabled()) {
                for (int i = 0; i < events.size(); i++) {
                    TicketmasterResponse.Evento ev = events.get(i);
                    String fecha = ev.dates() != null && ev.dates().start() != null
                                   ? ev.dates().start().localDate() : "sin fecha";
                    log.debug("[Reader]   [{}] nombre='{}' fecha='{}'", i + 1, ev.name(), fecha);
                }
            }

        } catch (Exception e) {
            log.error("[Reader] Error llamando a Ticketmaster API (página {}): {} — {}",
                      page + 1, e.getClass().getSimpleName(), e.getMessage());
        }
    }
}

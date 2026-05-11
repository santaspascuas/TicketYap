package com.trabajofinal.ticketyap.batch.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TicketmasterResponse(
    @JsonProperty("_embedded") Embedded embedded,
    Page page
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Embedded(List<Evento> events) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Evento(
        String name,
        Dates dates,
        @JsonProperty("_embedded") VenueEmbedded embedded,
        List<Imagen> images
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Imagen(
        String url,
        String ratio,
        int width,
        int height,
        boolean fallback
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Dates(Start start) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Start(String localDate) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record VenueEmbedded(List<Venue> venues) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Venue(
        String name,
        Address address,
        City city,
        String postalCode
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Address(String line1) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record City(String name) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Page(int totalPages, int number) {}
}

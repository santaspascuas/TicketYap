package com.trabajofinal.ticketyap.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trabajofinal.ticketyap.modelos.DTO.OfertaDTO;
import com.trabajofinal.ticketyap.modelos.DTO.OfertaRequest;
import com.trabajofinal.ticketyap.response.ApiResponse;
import com.trabajofinal.ticketyap.response.ResponseUtil;
import com.trabajofinal.ticketyap.service.OfertaService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/oferta")
public class OfertaController {

    private final OfertaService ofertaService;

    public OfertaController(OfertaService ofertaService) {
        this.ofertaService = ofertaService;
    }

    @PostMapping("/hacer")
    public ResponseEntity<ApiResponse<OfertaDTO>> crearOferta(
            HttpServletRequest request,
            @RequestBody OfertaRequest body) {

        OfertaDTO resultado = ofertaService.crearOferta(body);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ResponseUtil.sucess(resultado, "Oferta enviada correctamente", request.getRequestURI()));
    }

    @GetMapping("/entrada/{idEntrada}")
    public ResponseEntity<ApiResponse<List<OfertaDTO>>> ofertasPorEntrada(
            HttpServletRequest request,
            @PathVariable Integer idEntrada) {

        List<OfertaDTO> resultado = ofertaService.obtenerOfertasPorEntrada(idEntrada);
        return ResponseEntity.ok(
            ResponseUtil.sucess(resultado, "Ofertas de la entrada", request.getRequestURI()));
    }

    @GetMapping("/mis-ofertas")
    public ResponseEntity<ApiResponse<List<OfertaDTO>>> misOfertas(HttpServletRequest request) {
        List<OfertaDTO> resultado = ofertaService.obtenerMisOfertas();
        return ResponseEntity.ok(
            ResponseUtil.sucess(resultado, "Mis ofertas enviadas", request.getRequestURI()));
    }

    @PutMapping("/{id}/aceptar")
    public ResponseEntity<ApiResponse<Void>> aceptar(
            HttpServletRequest request,
            @PathVariable Integer id) {

        ofertaService.aceptarOferta(id);
        return ResponseEntity.ok(
            ResponseUtil.sucess(null, "Oferta aceptada", request.getRequestURI()));
    }

    @PutMapping("/{id}/rechazar")
    public ResponseEntity<ApiResponse<Void>> rechazar(
            HttpServletRequest request,
            @PathVariable Integer id) {

        ofertaService.rechazarOferta(id);
        return ResponseEntity.ok(
            ResponseUtil.sucess(null, "Oferta rechazada", request.getRequestURI()));
    }
}

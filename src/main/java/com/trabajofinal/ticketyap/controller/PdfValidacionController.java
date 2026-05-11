package com.trabajofinal.ticketyap.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.trabajofinal.ticketyap.modelos.DTO.PdfValidacionResultado;
import com.trabajofinal.ticketyap.response.ApiResponse;
import com.trabajofinal.ticketyap.response.ResponseUtil;
import com.trabajofinal.ticketyap.service.PdfValidacionService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/pdf")
public class PdfValidacionController {

    private final PdfValidacionService pdfValidacionService;

    public PdfValidacionController(PdfValidacionService pdfValidacionService) {
        this.pdfValidacionService = pdfValidacionService;
    }

    @PostMapping("/validar")
    @PreAuthorize("hasRole('ROLE_PRO')")
    public ResponseEntity<ApiResponse<PdfValidacionResultado>> validar(
            HttpServletRequest request,
            @RequestParam("archivo") MultipartFile archivo) {

        PdfValidacionResultado resultado = pdfValidacionService.validar(archivo);
        return ResponseEntity.ok(
            ResponseUtil.sucess(resultado, "Validación completada", request.getRequestURI())
        );
    }
}

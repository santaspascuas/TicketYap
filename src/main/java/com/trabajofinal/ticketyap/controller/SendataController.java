package com.trabajofinal.ticketyap.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.trabajofinal.ticketyap.modelos.DTO.FileResponse;
import com.trabajofinal.ticketyap.response.ApiResponse;
import com.trabajofinal.ticketyap.response.ResponseUtil;
import com.trabajofinal.ticketyap.service.SendataService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/sendata")
public class SendataController {

    private final SendataService sendataService;

    public SendataController(SendataService sendataService) {
        this.sendataService = sendataService;
    }

    @PostMapping("/updatefile")
    public ResponseEntity<ApiResponse<FileResponse>> subirPdf(
            @RequestParam("file") MultipartFile archivo,
            HttpServletRequest request) {

        FileResponse resultado = sendataService.procesarPdf(archivo);
        return ResponseEntity.ok(
            ResponseUtil.sucess(resultado, "PDF procesado correctamente", request.getRequestURI())
        );
    }
}

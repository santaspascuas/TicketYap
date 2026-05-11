package com.trabajofinal.ticketyap.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import com.trabajofinal.ticketyap.response.ApiResponse;
import com.trabajofinal.ticketyap.response.ResponseUtil;
import com.trabajofinal.ticketyap.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import com.trabajofinal.ticketyap.modelos.DTO.UserDto;


@RestController
@RequestMapping("admin")
public class AdminController {

    private final UserService userservice;

    public AdminController(UserService userservice){
        this.userservice = userservice;
    }

    @PreAuthorize("hasRole('ROLE_PRO')")
    @GetMapping("/usuariosRecientes")
    public ResponseEntity<ApiResponse<List<UserDto>>> obtenerUsuariosRecientes(HttpServletRequest request){
        return ResponseEntity.ok(
            ResponseUtil.sucess(
                userservice.obtenerUsuariosRecientesDto(),
                "Usuarios registrados recientemente obtenidos por el servidor",
                request.getRequestURI()
            )
        );
    }

    }



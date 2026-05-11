package com.trabajofinal.ticketyap.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trabajofinal.ticketyap.modelos.DTO.CarritoConItemsDTO;
import com.trabajofinal.ticketyap.response.ApiResponse;
import com.trabajofinal.ticketyap.response.ResponseUtil;
import com.trabajofinal.ticketyap.service.CarritoService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/carrito")
public class CarritoController {

    private final CarritoService carritoService;

    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<CarritoConItemsDTO>> obtenerCarrito(HttpServletRequest request) {
        return ResponseEntity.ok(
            ResponseUtil.sucess(
                carritoService.obtenerCarritoUsuario(),
                "Carrito del usuario",
                request.getRequestURI()
            )
        );
    }

    @PostMapping("/agregar/{id_entrada}")
    public ResponseEntity<ApiResponse<CarritoConItemsDTO>> agregarEntrada(
            HttpServletRequest request,
            @PathVariable Integer id_entrada) {
        return ResponseEntity.ok(
            ResponseUtil.sucess(
                carritoService.agregarEntrada(id_entrada),
                "Entrada agregada al carrito",
                request.getRequestURI()
            )
        );
    }

    @DeleteMapping("/item/{id_item}")
    public ResponseEntity<ApiResponse<CarritoConItemsDTO>> eliminarItem(
            HttpServletRequest request,
            @PathVariable Integer id_item) {
        return ResponseEntity.ok(
            ResponseUtil.sucess(
                carritoService.eliminarItem(id_item),
                "Item eliminado del carrito",
                request.getRequestURI()
            )
        );
    }

    @DeleteMapping("/vaciar")
    public ResponseEntity<ApiResponse<CarritoConItemsDTO>> vaciarCarrito(HttpServletRequest request) {
        return ResponseEntity.ok(
            ResponseUtil.sucess(
                carritoService.vaciarCarrito(),
                "Carrito vaciado",
                request.getRequestURI()
            )
        );
    }

    @PutMapping("/abandonar")
    public ResponseEntity<ApiResponse<Void>> abandonarCarrito(HttpServletRequest request) {
        carritoService.abandonarCarrito();
        return ResponseEntity.ok(
            ResponseUtil.sucess(null, "Carrito abandonado", request.getRequestURI())
        );
    }
}

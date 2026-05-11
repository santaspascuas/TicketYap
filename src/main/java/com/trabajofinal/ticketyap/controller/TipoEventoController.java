package com.trabajofinal.ticketyap.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trabajofinal.ticketyap.modelos.TipoEvento;
import com.trabajofinal.ticketyap.response.ApiResponse;
import com.trabajofinal.ticketyap.response.ResponseUtil;
import com.trabajofinal.ticketyap.service.TipoEventoService;

import jakarta.servlet.http.HttpServletRequest;



@RestController
@RequestMapping("tipoevento")
public class TipoEventoController {

    private final TipoEventoService  tpeventoservice;


    public TipoEventoController(TipoEventoService  tpeventoservice){
        this.tpeventoservice = tpeventoservice;
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<TipoEvento>>>getAlltipoEvento(HttpServletRequest request){
        return ResponseEntity.ok(
            ResponseUtil.sucess(
                tpeventoservice.ListadeTipoEventos(),
                "Lista de los Tipos de Eventos",
                request.getRequestURI()
            ));       
    }
    

    @PostMapping("/insert")
    public ResponseEntity<ApiResponse<?>>insertarEvento(@RequestBody TipoEvento evento, HttpServletRequest request){
        tpeventoservice.InsertarTipoEvento(evento);
        return ResponseEntity.ok(
            ResponseUtil.sucess(
            evento, 
            "Insertamos el eventoTipo", 
            request.getRequestURI())
        );
    }

    @DeleteMapping("/delete/{tipo_evento_id}")
    public ResponseEntity<ApiResponse<?>>eliminarEventoTipo(@RequestBody TipoEvento evento, HttpServletRequest request){
        tpeventoservice.EliminarTipoEvento(evento.getTipo_evento_id());
            return ResponseEntity.ok(
                ResponseUtil.sucess(
                    "Tipo Evento Eliminado", 
                    "Se ha eliminado el elemento de la base", 
                    request.getRequestURI())
            );
    }
    
}

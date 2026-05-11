package com.trabajofinal.ticketyap.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.trabajofinal.ticketyap.modelos.Entrada;
import com.trabajofinal.ticketyap.response.ApiResponse;
import com.trabajofinal.ticketyap.response.ResponseUtil;
import com.trabajofinal.ticketyap.service.EntradaService;
import com.trabajofinal.ticketyap.modelos.DTO.EntradaFront;

import jakarta.servlet.http.HttpServletRequest;



@RestController
@RequestMapping("entrada")
public class EntradaController {

    //Utilizar el servcio para poder acceder a los datos

    private final EntradaService entradaservice;

    public EntradaController(EntradaService entradaservice){
        this.entradaservice = entradaservice;
    }

    //Aqui mapeamos los enpoiunts para los controladores


    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<Entrada>>> getAllEntradas(HttpServletRequest request){
        return ResponseEntity.ok(
            ResponseUtil.sucess(
                entradaservice.obtenerListaEntrada(),
                "Lista de entradas obtenidas por el servidor",
                request.getRequestURI()
        )
        );  
    }

    @PostMapping("/buscar/{id}")
    public ResponseEntity<ApiResponse<Entrada>>obtenerdataid(@PathVariable ("id") Integer id, HttpServletRequest request){
        return ResponseEntity.ok(
            ResponseUtil.sucess(
                entradaservice.obtenerEventosporID(id),
                "Entrada obtenida por el servidor",
                request.getRequestURI()
            )
        );
    }
    
    
    @PostMapping("/insert")
    public ResponseEntity<ApiResponse<String>> insertarNuevaEntrada(@RequestBody EntradaFront nuevaEntrada, HttpServletRequest request){
        return ResponseEntity.ok(
            ResponseUtil.sucess(
                entradaservice.insertarEntrada(nuevaEntrada),
                "Entrada insertada correctamente",
                request.getRequestURI()
            )   
        );
    }

    //Sacar las entradas disponibles para un eventos
    @GetMapping("/buscarevento/{id}")
    public ResponseEntity<ApiResponse<List<Entrada>>>obtenerEntradasDisponiblesEvento(@PathVariable ("id") Integer id, HttpServletRequest request){

        return ResponseEntity.ok(
            ResponseUtil.sucess(
                entradaservice.obtenerEntradasDisponiblesEvento(id),
                "Entradas disponibles obtenidas correctamente",
                request.getRequestURI()
            )
        );
    }
    @GetMapping("/propias")
    public ResponseEntity<ApiResponse<List<Entrada>>> obtenerEntradasPropias(HttpServletRequest request) {
        return ResponseEntity.ok(
            ResponseUtil.sucess(
                entradaservice.obtenerEntradasPropias(),
                "Entradas propias del usuario autenticado",
                request.getRequestURI()
            )
        );
    }

    @GetMapping("/compradas")
    public ResponseEntity<ApiResponse<List<Entrada>>> obtenerEntradasCompradas(HttpServletRequest request) {
        return ResponseEntity.ok(
            ResponseUtil.sucess(
                entradaservice.obtenerEntradasCompradas(),
                "Entradas compradas por el usuario autenticado",
                request.getRequestURI()
            )
        );
    }

}

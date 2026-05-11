package com.trabajofinal.ticketyap.controller;


import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trabajofinal.ticketyap.modelos.Localizacion;
import com.trabajofinal.ticketyap.response.ApiResponse;
import com.trabajofinal.ticketyap.response.ResponseUtil;
import com.trabajofinal.ticketyap.service.LocalizacionService;

import jakarta.servlet.http.HttpServletRequest;




@RestController
@RequestMapping("localizacion")
public class LocalizacionController {

    //Inyectamos el servicio para la extración de datos.

    private final LocalizacionService localizacionservice;

    public LocalizacionController(LocalizacionService localizacionservice){

        this.localizacionservice= localizacionservice;
    }


    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<Localizacion>>>getAllLocalizacion(HttpServletRequest request){
        return ResponseEntity.ok(
            ResponseUtil.sucess(
                localizacionservice.obtenerlistadoLoca(),
                "Listado de las localizaciones de los eventos",
                request.getRequestURI())
        );     
    }

    @PreAuthorize("hasRole('ROLE_PRO')")
    @PostMapping("/alta")
    public ResponseEntity<ApiResponse<Localizacion>>insertarLocalizacion(@RequestBody Localizacion local, HttpServletRequest request){

        return ResponseEntity.ok(
            ResponseUtil.sucess(
                localizacionservice.insertarLocalizacion(local),
                "Insertamos una localizacion en la database",
                request.getRequestURI())
        );
    }

    
    @PreAuthorize("hasRole('ROLE_PRO')")
    @DeleteMapping("/delete/{id_evento}")
    public ResponseEntity<ApiResponse<Void>>eliminarLocalizacion(@PathVariable ("id_evento") Integer id){
        localizacionservice.eliminarLocalizacion(id);
        return ResponseEntity.ok(
            ResponseUtil.sucess(
                null,
                "Se ha eliminado el evento ",
                null)
        );
    }

    @PreAuthorize("hasRole('ROLE_PRO')")
    @PutMapping("/update")
    public ResponseEntity<ApiResponse<Localizacion>>actualizarLocalizacion(@RequestBody Localizacion local, HttpServletRequest request){

        return ResponseEntity.ok(
            ResponseUtil.sucess(
                localizacionservice.insertarLocalizacion(local),
                "Actualizamos una localizacion en la database",
                request.getRequestURI())
        );
    }

}
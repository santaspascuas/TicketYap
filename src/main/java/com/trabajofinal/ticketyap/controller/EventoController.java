  package com.trabajofinal.ticketyap.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.trabajofinal.ticketyap.modelos.CombinaLocalizacionEvento;
import com.trabajofinal.ticketyap.modelos.Eventos;
import com.trabajofinal.ticketyap.response.ApiResponse;
import com.trabajofinal.ticketyap.response.ResponseUtil;
import com.trabajofinal.ticketyap.service.EventoImagenService;
import com.trabajofinal.ticketyap.service.EventoService;

import jakarta.servlet.http.HttpServletRequest;






@RestController
@RequestMapping("evento")
public class EventoController {

    /*
     * El controlador usa:
     * El servicio para sacar los datos del sql ->dao->implemetacion->bbdd
     */

     private final EventoService eventoService;
     private final EventoImagenService eventoimagenService;

     //Constructor con intecciones de dependencias


     public EventoController(EventoService eventoService, EventoImagenService eventoimagenService){
        this.eventoService = eventoService;
        this.eventoimagenService = eventoimagenService;
     }

     @GetMapping("/all")
     public ResponseEntity<ApiResponse<List<Eventos>>> getAllEventos(HttpServletRequest request){
        return ResponseEntity.ok(
            ResponseUtil.sucess(
                eventoService.obtenerListaEventos(),
                "Lista total de los eventos de la base de datos",
                request.getRequestURI()
                )
        );   
     }

     @GetMapping("/buscar/{id}")
     public ResponseEntity<ApiResponse<Eventos>>obtenerdataid(@PathVariable ("id") Integer id, HttpServletRequest request) {
      return ResponseEntity.ok(
         ResponseUtil.sucess(
            eventoService.obtenerEventoporid(id), 
            "Elemento obtenido por el buscador a través del id", 
            request.getRequestURI())
      );
     }
     @GetMapping("/buscar/{uuid}")
     public ResponseEntity<ApiResponse<Eventos>>obtenerdatauuid(@PathVariable ("uuid") UUID uuid, HttpServletRequest request) {
      return ResponseEntity.ok(
         ResponseUtil.sucess(
            eventoService.obtenerEventoporWebInt(uuid), 
            "Elemento obtenido por el buscador a través del uuid", 
            request.getRequestURI())
      );
     }
     @PreAuthorize("hasRole('ROLE_PRO')")
     @PostMapping("/insert")
     public ResponseEntity<ApiResponse<Eventos>>insertarEvento(@RequestBody Eventos eventos, HttpServletRequest request) {
         return ResponseEntity.ok(
            ResponseUtil.sucess(
               eventoService.insertarEvento(eventos),
               "Evento insertado con exito en la base de datos de eventos",
               request.getRequestURI())
         );
     }

     @PreAuthorize("hasRole('ROLE_PRO')")
     @DeleteMapping("/delete/{id}")
     public ResponseEntity<ApiResponse<String>>deletebyid(@PathVariable ("id") Integer id, HttpServletRequest request){
      return ResponseEntity.ok(
         ResponseUtil.sucess(
            eventoService.deleteEventobyid(id),
             "Se ha eliminado un evento",
             request.getRequestURI())
      );  
     }

     //CombinaEvento
     @GetMapping("/localizacion/{id}")
     public ResponseEntity<ApiResponse<List<CombinaLocalizacionEvento>>>listadoLocalizacion(@PathVariable ("id") Integer id, HttpServletRequest request){
      return ResponseEntity.ok(
         ResponseUtil.sucess(
            eventoService.eventoLocalizacion(id),
            "La localizacion del evento",
            request.getRequestURI())
      );
     }

     @GetMapping("/localizacion/uuid/{uuid}")
     public ResponseEntity<ApiResponse<List<CombinaLocalizacionEvento>>>listadoLocalizacionByUUID(@PathVariable ("uuid") java.util.UUID uuid, HttpServletRequest request){
      return ResponseEntity.ok(
         ResponseUtil.sucess(
            eventoService.eventoLocalizacion(
               eventoService.obtenerEventoporWebInt(uuid).getEvento_id()
            ),
            "La localizacion del evento por UUID",
            request.getRequestURI())
      );
     }

     @PreAuthorize("hasRole('ROLE_PRO')")
     @PutMapping("/update")
     public ResponseEntity<ApiResponse<Eventos>> updateEvento(@RequestBody Eventos eventos, HttpServletRequest request){
      return ResponseEntity.ok(
         ResponseUtil.sucess(
            eventoService.actualizarEvento(eventos),
            "Se actualiza el evento" + eventos.getNombre_evento() +  "En la base de datos",
            request.getRequestURI())
      );

     }

     @PreAuthorize("hasRole('ROLE_PRO')")
     @PostMapping("/subirImagen")
     public ResponseEntity<ApiResponse<String>>subirIMagendeEvento(HttpServletRequest request, @RequestParam("file") MultipartFile file, @RequestParam("id_evento") Integer id_evento){
      System.out.println("El id del evento es: " + id_evento);
      System.out.println("El nombre del archivo es: " + file.getOriginalFilename());
      
      return ResponseEntity.ok(
            ResponseUtil.sucess(
               eventoimagenService.InsertarEventoImagen(new MultipartFile[]{file}, id_evento),
               "Se ha insertado la imagen del evento en la base de datos",
               request.getRequestURI())
         ); 
     }


     @GetMapping("/allImagenes")
     public ResponseEntity<ApiResponse<List<?>>> obtenerTodasImagenes(HttpServletRequest request){
         return ResponseEntity.ok(
            ResponseUtil.sucess(
               eventoimagenService.buscarTodasImagenes(),
               "Se han obtenido todas las imagenes de la base de datos",
               request.getRequestURI())
         ); 
     }

}

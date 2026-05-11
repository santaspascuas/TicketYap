 package com.trabajofinal.ticketyap.service;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trabajofinal.ticketyap.dao.EventoDao;
import com.trabajofinal.ticketyap.exceptions.NotFoundException;
import com.trabajofinal.ticketyap.modelos.CombinaLocalizacionEvento;
import com.trabajofinal.ticketyap.modelos.Eventos;

@Service
public class EventoService {

    private final EventoDao eventodao;
    private static final Logger log = LoggerFactory.getLogger(EventoService.class);


    public EventoService(EventoDao eventodao){
        this.eventodao = eventodao;
    }

    public List<Eventos> obtenerListaEventos(){
        return eventodao.selectEventos();
    }

    public List<CombinaLocalizacionEvento>eventoLocalizacion(Integer idevento){
        return eventodao.combinaEvento(idevento);
    }



    public Eventos obtenerEventoporid(Integer idEvento){
        log.info("Se inicia la funcion de obtenerEventoporid");
        log.debug("Se ha recibido:", idEvento);

         Eventos eventoDevuelto = eventodao.selectEventoByid(idEvento).orElseThrow(
            () ->
            new NotFoundException(
                    String.format("El evento con el id '%s' no se encuentra disponible", idEvento)
         ));
         return eventoDevuelto;
    }

    //Todo el de obtener por nombre

    public Eventos obtenerEventoporNombre(String nombre){
        log.info("Se inicia la funcion de obtenerEventoporNombre");
        log.debug("Se ha recibido:", nombre);

        Eventos eventoEncontrado = eventodao.selectEventoByname(nombre).orElseThrow(
            ()->
            new NotFoundException(
            String.format("El usuario con nombre '%s' no se encuentra disponible", nombre)
             ));
        return eventoEncontrado;     
    }

    @Transactional
    public Eventos insertarEvento(Eventos eventoinsert){
        log.info("Se inicia la funcion de insertarEvento");
        log.debug("Se ha recibido:{}", eventoinsert);

        if(eventodao.selectEventoByname(eventoinsert.getNombre_evento()).isPresent()){
            log.warn("Intento de insertar evento duplicado: {}"+ "eventoinsert.getNombre_evento()");
            throw  new IllegalStateException("El usuario con nombre" + eventoinsert.getNombre_evento() + "Ya existe.");
        }
        //Aqui lo cazaria el uuiid
        eventoinsert.setWebident(UUID.randomUUID());
        log.debug("UUID asignado al evento: {}",eventoinsert.getWebident());
        //Ahora seria integrar si este no existe
        Integer  insertarEvento = eventodao.insertarEvento(eventoinsert);

        if(insertarEvento !=1){
            //Si ha habido mas de dos inserciones, mensaje de error.
            log.error("Error al insertar el evento '{}'", eventoinsert.getNombre_evento());
            throw new IllegalStateException("Error al insertar el evento  en la base de datos.");
        }
        //Si todo sale bien. Deberiamos de traer lo insertado mediante la busqueda en la base de datos o bien replicamos lo que hemos insertado
        //En este caso simplemente retornamos lo que hemos printeado de inserción.

        log.info("Evento '{}' insertado correctamente", eventoinsert.getNombre_evento());
        return  obtenerEventoporNombre(eventoinsert.getNombre_evento());
    }

    @Transactional
    public String deleteEventobyid( Integer eventid){
        log.info("Se inicia la funcion de deleteEventobyid");
        log.debug("Se ha recibido:", eventid);
        if(eventodao.selectEventoByid(eventid).isEmpty()){
            log.warn("Se ha borrado el evento seleccionado:", eventid);
            throw new NotFoundException(
                    String.format("El evento con id '%s' no existe", eventid)
            );
        }
        //Ejecuta la eliminacion.
        Integer eliminado = eventodao.deleteEvento(eventid);

        if(eliminado !=1){
            log.error("Error al borrar el evento");
            throw new IllegalStateException("Error al eliminar el evento.");
        }
        log.info("Se ha borrado el evento:",eventid);
            return  "UsuarioAdmin elimina evento";
    }
    @Transactional
    public Eventos actualizarEvento(Eventos eventoupdate){
        log.info("Se inicia la funcion de actualizarEvento");
        log.debug("Se ha recibido:{}", eventoupdate);

        if(eventodao.selectEventoByid(eventoupdate.getEvento_id()).isEmpty()){
            log.warn("Se busca el evento en la data:{}", eventoupdate.getEvento_id());
            //Sino esta en la base de datos
             throw new NotFoundException(
                    String.format("El evento con id '%s' no existe", eventoupdate.getEvento_id()));
        }
        Integer actualizado = eventodao.updateEvento(eventoupdate);
        if(actualizado != 1){
            log.error("Error al actualizar el evento '{}'", eventoupdate.getNombre_evento());
            throw new IllegalStateException("Error al actualizar el evento.");
        }
        log.info("Se finaliza el update:",eventoupdate.getEvento_id());
        return obtenerEventoporNombre(eventoupdate.getNombre_evento());
    }
    @Transactional
    public Eventos obtenerEventoporWebInt(UUID webInt){
        log.info("Se inicia la funcion de obtenerEventoporWebInt");
        log.debug("Se ha recibido:", webInt);
                return eventodao.selectEventoByUUID(webInt)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format("No existe evento con UUID '%s'", webInt)
                        ));
        }
    }

    



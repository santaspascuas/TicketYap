package com.trabajofinal.ticketyap.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.trabajofinal.ticketyap.dao.TipoEventoDao;
import com.trabajofinal.ticketyap.exceptions.ListaEventoException;
import com.trabajofinal.ticketyap.modelos.TipoEvento;

@Service
public class TipoEventoService {

    private static final Logger log = LoggerFactory.getLogger(TipoEventoService.class);


    private final TipoEventoDao eventoDao;

    public TipoEventoService(TipoEventoDao eventoDao){
        this.eventoDao = eventoDao;
    }


    public List<TipoEvento> ListadeTipoEventos(){
        return eventoDao.listarTipoEvento();   
    }

    public void InsertarTipoEvento(TipoEvento evento){
        log.info("Iniciando inserción de tipo de evento");
        log.debug("Datos recibidos: {}", evento);

        if(eventoDao.ConfirmaEvento(evento.getNombre())){
            throw new ListaEventoException("El tipo de evento ya existe");
        }

        TipoEvento eventosinid = new TipoEvento(evento.getNombre());

        if(eventoDao.insertarTipoEvento(eventosinid) !=1){
            throw new ListaEventoException("Error al insertar el tipo de evento");
        } 
        log.info("Tipo de evento '{}' insertado correctamente", evento.getNombre());
    }


    public void EliminarTipoEvento(Integer id){
        log.info("Eliminando tipo de evento con id {}", id);

        if(eventoDao.deleteTipoEvento(id) !=1){
            throw new ListaEventoException("Problema al eliminar el tipo de evento");
        }
        log.info("Tipo de evento {} eliminado correctamente", id);
    }





}

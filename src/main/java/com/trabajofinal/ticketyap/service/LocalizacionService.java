package com.trabajofinal.ticketyap.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.trabajofinal.ticketyap.dao.LocalizacionDao;
import com.trabajofinal.ticketyap.exceptions.NotFoundException;
import com.trabajofinal.ticketyap.modelos.Localizacion;

@Service
public class LocalizacionService {


    //Inyectamos la dependencia para usar el jdbctemplate

    private final LocalizacionDao localizaciondao;
    
    public LocalizacionService(LocalizacionDao localizaciondao){
        this.localizaciondao = localizaciondao;
    }
  
    public List<Localizacion> obtenerlistadoLoca(){
        
        return localizaciondao.selectLocalizacion();  
    }

    public Localizacion obtenerlocalizacionid(Integer id){

        Localizacion  devueltalocal = localizaciondao.buscarlocalizacion(id).orElseThrow(
            () ->
            new NotFoundException(
                    String.format("El usuario con el id '%s' no se encuentra disponible", id)

        ));
        return  devueltalocal;
    }

    public Localizacion insertarLocalizacion (Localizacion localizacion) {

        ///Insertar localizaciones
        /// Primero vamos a comprobar si ya existe.
        /// 
        if(localizaciondao.buscarpornombre(localizacion.getNombre()).isPresent()){
            // Si existe, nos va a decir que existe y tirara un aviso
            throw  new IllegalStateException("La localización ya existe" + localizacion.getNombre() + "Ya existe.");
        }
        Integer introducido = localizaciondao.insertarLocalizacion(localizacion);

        if(introducido !=1){
            throw new IllegalStateException("Error al insertar el cliente en la base de datos.");
        }
        //Si todo ha salido bien lo que podemos hacer es devolver lo creado.

        return localizaciondao.buscarpornombre(localizacion.getNombre()).get();
    }


    public void eliminarLocalizacion (Integer id) {
        //Buscarlo y eliminarlo
        if(!localizaciondao.buscarlocalizacion(id).isPresent()){
           throw new IllegalStateException("No existe esa localizacion en la base de datos" + id);
        }
        Integer eliminado = localizaciondao.deleteLocalizacion(id);
        if(eliminado!=1){
            throw new IllegalStateException("Se ha producido un problema en la eliminacion de la localizacion");
        }
    }

    //Actualizar

    public Localizacion updateLocalizacion(Localizacion localizacion){
        //v2
        Localizacion localizada = localizaciondao.buscarlocalizacion(localizacion.getLocalizacion_id()).orElseThrow(
         () ->
            new IllegalStateException("Error al insertar el cliente en la base de datos.")
        );
        //Si lo encuentra empezamos a setterarlo
        localizada.setNombre(localizacion.getNombre());
        localizada.setCalle(localizacion.getCalle());
        localizada.setMunicipio(localizacion.getMunicipio());
        localizada.setLocalidad(localizacion.getLocalidad());
        localizada.setCodigo_postal(localizacion.getCodigo_postal());

        //Lo introducimos

        Integer actualizado = localizaciondao.UpdateLocalizacion(localizada);

        if(actualizado !=1){
             throw new IllegalStateException("Se ha  producido un error en la actualizacion del usuario");
        }
        Localizacion devolucion = localizaciondao.buscarpornombre(localizada.getNombre()).orElseThrow(
            () -> new IllegalStateException("No se pudo recuperar la localizacion actualizada"));
            return devolucion;
    }







}

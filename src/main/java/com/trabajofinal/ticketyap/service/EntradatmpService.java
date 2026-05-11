package com.trabajofinal.ticketyap.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trabajofinal.ticketyap.dao.EntradatmpDao;
import com.trabajofinal.ticketyap.exceptions.EntradaTmpException;
import com.trabajofinal.ticketyap.modelos.Entradatmp;


@Service
public class EntradatmpService {

    private final EntradatmpDao entradatmpdao;
    private static final Logger log = LoggerFactory.getLogger(EntradatmpService.class);
    
    public EntradatmpService(EntradatmpDao entradatmpdao){
        this.entradatmpdao = entradatmpdao;
    }


    //Empezamos a realizar las funciones del servicio
    
    @Transactional
    public Integer insertarEntradaTmp(Entradatmp entradatmp){
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("[{}] Iniciamos el metodo", method);
        log.debug("recibimos la entrada {}",entradatmp.toString());
        String hash = entradatmp.getHash();
        //LLamo al servicio.
        if(entradatmpdao.buscaEntradatmpbyhash(hash)){
            log.error("Error ya esta creado con ese hash");
            throw new RuntimeException("Ya existe una entrada temporal con el mismo hash en la base de datos");
        }
        Integer resultado = entradatmpdao.insertartmpfile(entradatmp);
        //Esto ahora devuelve el id de la insercion.
        if(resultado == null){
            throw new RuntimeException("Error al insertar la entrada temporal en la base de datos");
        }

        return resultado;
    }

    //Quiero devolver el objeto. //El servicio desempaqueta
    
    @Transactional
    public Entradatmp buscarEntradabyHash(String hash){
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("[{}] Iniciamos el metodo", method);
        log.debug("recibimos el hash {}",hash);
        if(hash.isEmpty()){
            throw new EntradaTmpException("No envias el hash en la llamada.");
        }
        return entradatmpdao.obtenerEntradabyHash(hash).orElseThrow(
            ()-> new EntradaTmpException("No existe esa entrada en la database")
        );
    }



}

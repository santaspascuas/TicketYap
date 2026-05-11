package com.trabajofinal.ticketyap.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.trabajofinal.ticketyap.dao.EntradaDao;
import com.trabajofinal.ticketyap.exceptions.NotFoundException;
import com.trabajofinal.ticketyap.modelos.DTO.EntradaFront;
import com.trabajofinal.ticketyap.modelos.DTO.UserDto;
import com.trabajofinal.ticketyap.modelos.Entrada;
import com.trabajofinal.ticketyap.modelos.Entradatmp;


@Service
public class EntradaService {

    //Inyectamos la dependencia del dao
    private final EntradaDao entradado;
    private final UserService userservice;
    private final EntradatmpService entradatmpservice;
    private static final Logger log = LoggerFactory.getLogger(EntradaService.class);

    public EntradaService(EntradaDao entradado, UserService userservice,EntradatmpService entradatmpservice){
        this.entradado = entradado;
        this.userservice = userservice;
        this.entradatmpservice = entradatmpservice;
    }


    public List<Entrada> obtenerListaEntrada(){
        return entradado.selectEntradas();
    }

    public Entrada obtenerEventosporID(Integer idEvento){
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("[{}] Iniciamos el metodo", method);
        log.debug("recibimos los datos:", idEvento);

        Entrada entradaDevuelta = entradado.selectEntradabyuser(idEvento).orElseThrow(
           ()->
           new NotFoundException(String.format(
            "La entrada no se encuentra con ese id" + idEvento)) 
        );
        return entradaDevuelta;
    }

    public String  insertarEntrada( EntradaFront nuevaEntrada){
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("[{}] Iniciamos el metodo", method);
        log.debug("Los datos de la entrada:{}", nuevaEntrada.toString());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto idUsuario = userservice.obtenerUsuarioporEmail(auth.getName());
        Integer id_vendedor = idUsuario.getId_usuario();
        //Antes de meter la entrada
        String hashEntrada = nuevaEntrada.getHash();
        log.debug("String hashEntrada:{}--", hashEntrada);
        Entradatmp encuentra = entradatmpservice.buscarEntradabyHash(hashEntrada);
        log.debug("Entradatmp encuentr--", hashEntrada);

        //¿Cuando encuentra la entrada? revisamos el numero de paginas y metemos esas entradas.
        Integer id_pdf = encuentra.getId_entrada();
        //Luego recorremos eh insertamos.
        for(int i =0; i< encuentra.getNum_pagin(); i++){
            Entrada entradaGuardar = new Entrada(nuevaEntrada.getId_evento(), id_vendedor, nuevaEntrada.getPrecio_venta(), "Disponible", nuevaEntrada.getFecha_publicacion(),id_pdf);
            Integer resultado = entradado.insertarEntrada(entradaGuardar);
        if(resultado !=1){
            throw new IllegalStateException("Hubo un problema en la insercion de la base de datos");
        }

     }
        return "Entrada creada correctamente";
    }

    public String eliminarEntrada(Integer idUsuario, Integer idEntrada){
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("[{}] Iniciamos el metodo: ", method);
        log.debug("Integer idUsuario:",idUsuario, "+ ", "idEntrada" , idEntrada);
        
        if(entradado.selectEntradabyuser(idEntrada).isEmpty()){
            throw new NotFoundException("La entrada no existe en la base de datos");
        }
        //Si existe, la borramos
        Integer resultadoBorrado = entradado.deleteEntrada(idUsuario, idEntrada);
        
        if(resultadoBorrado !=1){
            throw new IllegalStateException("Hubo un problema en el borrado de la base de datos");
        }
        return "Entrada eliminada correctamente";
    }

    public List<Entrada> obtenerEntradasDisponiblesEvento(Integer idEvento){
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("[{}] Iniciamos el metodo: ", method);
        log.debug("Estamos paso :public List<Entrada> obtenerEntradasDisponiblesEvento" +idEvento);
        return entradado.selectEntradadisponiblebyid(idEvento);
    }


    public List<Entrada> obtenerEntradasPropias() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto usuario = userservice.obtenerUsuarioporEmail(auth.getName());
        return entradado.selectEntradasPropias(usuario.getId_usuario());
    }

    public List<Entrada> obtenerEntradasCompradas() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto usuario = userservice.obtenerUsuarioporEmail(auth.getName());
        return entradado.selectEntradasCompradas(usuario.getId_usuario());
    }

    public Integer ActualizarEstadoCompraEntrada(Integer idEntrada, Integer iduser, String estado){
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("[{}] Iniciamos el metodo: ", method);
        log.debug("{}", idEntrada, "{}",iduser, "{}", estado);
        if(!entradado.selectEntradabyuser(idEntrada).isPresent()){
            throw new NotFoundException("La entrada no existe en la base de datos");
        }
        Integer updateentradacompra = entradado.updateEstadoCompra(idEntrada, iduser, estado);
        if(updateentradacompra!=1){
             throw new IllegalStateException("Hubo un problema en la actualizacion del estado de la entrada");
        }
        return updateentradacompra;
    }

    



    




}

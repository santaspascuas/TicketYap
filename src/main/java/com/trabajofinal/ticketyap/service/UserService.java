package com.trabajofinal.ticketyap.service;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.trabajofinal.ticketyap.dao.UserDao;
import com.trabajofinal.ticketyap.exceptions.NotFoundException;
import com.trabajofinal.ticketyap.modelos.DTO.UserDto;
import com.trabajofinal.ticketyap.modelos.DTO.UserProps;
import com.trabajofinal.ticketyap.modelos.AuthModel;
import com.trabajofinal.ticketyap.modelos.User;

@Service
public class UserService {

    //Usamos el dao
    
    private final UserDao userdao;
    private final PasswordEncoder encoder;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    //inyectamos las dependencias para usarlas.
    public UserService(UserDao userdao, PasswordEncoder encoder){
        this.userdao = userdao;
        this.encoder= encoder;
    }
    //Usamos las extracciones que realizamos con la implementacion

    public List<UserDto> obtenerUsuarios(){
        //Implementar un DTO
        //Creamos una lista y luego creamos el dto
        List<UserDto>usuariosDto = userdao.selectUsers().stream().map(
            usuario ->
            new UserDto(
                usuario.getId_usuario(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getNumero_cuenta(),
                usuario.getRole())).toList();

        return  usuariosDto;
    }

    //Obtener la informacion del usuario por correo.

    public UserDto obtenerUsuarioporEmail(String email){

       User userdelabase = userdao.selectUserbyemail(email)
            .orElseThrow(() -> new NotFoundException(
                    String.format("El usuario con email '%s' no se encuentra disponible", email)
            ));
        return new UserDto(
            userdelabase.getId_usuario(),
            userdelabase.getNombre(),
            userdelabase.getEmail(),
            userdelabase.getNumero_cuenta(),
            userdelabase.getRole()
            );
    }

    public UserDto insertarUsuario(User usuariopost){
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("[{}] Inicio", method); 
        log.debug("Se recibe el usuario:{}",usuariopost );
        
        if(userdao.selectUserbyemail(usuariopost.getEmail()).isPresent()){
            log.error("Se busca al usuario por el correo",method);
            throw  new IllegalStateException("El usuario con nombre" + usuariopost.getNombre() + "Ya existe.");
        }
        usuariopost.setContrasena(encoder.encode(usuariopost.getContrasena()));
        log.debug("Hasheamos la contrasena", usuariopost.getContrasena());

        //Generamos la fecha del backend.

        usuariopost.setFecha_registro(LocalDate.now());

        Integer confirmacion = userdao.insertarUsuarioByAdmin(usuariopost);
        //Si el integer es distinto de uno es que hemos hecho mal. 
        if(confirmacion!=1){
             log.error("[{}] Error al insertar usuario en la base de datos", method);
            throw new IllegalStateException("Error al insertar el cliente en la base de datos.");
        }
        log.info("[{}] Usuario insertado correctamente: {}", method, usuariopost.getEmail());

        return obtenerUsuarioporEmail(usuariopost.getEmail());
    }

    //Eliminar usuario de la base de datos.

    public String eliminarUsuario(Integer ideliminado){
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("[{}] Inicio", method); 
        log.debug("Se recibe el usuario:{}",ideliminado);
    if(userdao.selectusuariobyid(ideliminado).isEmpty() ){
        log.error("[{}] Error el usuario no existe", method);
           throw new IllegalStateException("Error no existe ese usuario en la base de datos" + ideliminado);
    }
     Integer eliminado = userdao.deleteUser(ideliminado);
    if(eliminado !=1){
        log.error("[{}] Error al insertar usuario en la base de datos", method);
     throw new IllegalStateException("No se ha eliminado correctamente de la base de datos");
     }
     return  "Se ha eliminado el usuario" + ideliminado;   
    }


    public User loguinUserSecurity(String email){
        System.out.println("Estoy validando el email" +email);
        return userdao.selectUserbyemail(email).orElseThrow(
            () -> new NotFoundException(
                    String.format("El usuario con email '%s' no se encuentra disponible", email)
        ));
    }

    
    public UserDto updateUsuaiDto(User usuarioupdate){
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("[{}] Inicio", method); 
        log.debug("Se recibe el usuario:{}",usuarioupdate.toString());

        User userobten = userdao.selectUserbyemail(usuarioupdate.getEmail()).map(
            usuarioviejo ->{
                usuarioviejo.setNombre(usuarioupdate.getNombre());
                usuarioviejo.setEmail(usuarioupdate.getEmail());
                usuarioviejo.setContrasena(usuarioupdate.getContrasena());
                usuarioviejo.setNumero_cuenta(usuarioupdate.getNumero_cuenta());
                usuarioviejo.setRole(usuarioupdate.getRole());
                //actualiza
                Integer update = userdao.updateUser(usuarioviejo);

                //verifica
                if(update !=1){
                    log.error("[{}] Error el usuario no se ha actualizado", method);
                    throw new IllegalStateException("Error al actualizar datos del cliente");
                }
                return usuarioviejo;
            }).orElseThrow(() -> new NotFoundException(
                    String.format("El usuario con email '%s' no se encuentra disponible", usuarioupdate.getEmail())
            ));

            return obtenerUsuarioporEmail(userobten.getEmail());
    }


    public UserDto actualizarUsuarioPropio(String email, UserDto usuariopropio){
        //Le pasamos el email. Buscamos esa persona
        User userobtenido = userdao.selectUserbyemail(email).orElseThrow(
            () -> new NotFoundException(
                    String.format("El usuario con email '%s' no se encuentra disponible", email)
            ));
        //Una vez obtenido actualizamos
        userobtenido.setNombre(usuariopropio.getNombre());
        userobtenido.setNumero_cuenta(usuariopropio.getNumero_cuenta());
        //actualizamos
        Integer update = userdao.updateUser(userobtenido);
        
        if(update !=1){
            throw new IllegalStateException("Error al actualizar los datos del cliente");
        }
        return obtenerUsuarioporEmail(userobtenido.getEmail());

    }

    public Boolean cambiarContrasenaUsuario(AuthModel modelo){
        Integer updatecontrasena = userdao.cambiarContrasenaUsuario(modelo);
           if(updatecontrasena !=1){
                    throw new IllegalStateException("Error al actualizar la contraseña del usuario");
            }
        return true;
    }

    public List<UserDto> obtenerUsuariosRecientesDto(){
        return userdao.Usuarioregistradosrecientes().stream()
            .map(u -> new UserDto(u.getId_usuario(), u.getNombre(), u.getEmail(), u.getNumero_cuenta(), u.getRole()))
            .toList();
    }

    public UserProps  obtenerDatosUsuario(String email){
        
        User usuarioPropio = userdao.selectUserbyemail(email)
        .orElseThrow(
            () ->new NotFoundException(String.format("El usuario con email '%s' no se encuentra disponible", email))
        );
        return new UserProps(
        usuarioPropio.getEmail(), 
        usuarioPropio.getId_usuario(), 
        usuarioPropio.getNombre(), 
        usuarioPropio.getNumero_cuenta());
    }
}

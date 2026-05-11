package com.trabajofinal.ticketyap.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trabajofinal.ticketyap.modelos.AuthModel;
import com.trabajofinal.ticketyap.modelos.DTO.UserDto;
import com.trabajofinal.ticketyap.modelos.User;
import com.trabajofinal.ticketyap.response.ApiResponse;
import com.trabajofinal.ticketyap.response.ResponseUtil;
import com.trabajofinal.ticketyap.service.UserService;

import jakarta.servlet.http.HttpServletRequest;





@RestController
@RequestMapping("user")
public class UserController {

    //Inyectamos el servicio para poder usar la extraccion de datos

    private final UserService  uservice;

    public UserController(UserService uservice){
        this.uservice = uservice;
    }

    // Es una respuesta que utiliza una clase response. Esta clase response es como una plantilla para responder a las respuestas correctas del servidor.

    @PreAuthorize("hasRole('ROLE_PRO')")
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<UserDto>>> getallUsers(HttpServletRequest request){
        //Obtenemos el path de forma automatica
        return ResponseEntity.ok(ResponseUtil.sucess(uservice.obtenerUsuarios()
        , "Actualización de los usuarios totales"
        , request.getRequestURI()));
    }
    @PreAuthorize("hasRole('ROLE_PRO')")
     @GetMapping("/email/{email}")
     public ResponseEntity<ApiResponse<UserDto>>getinfoUser(HttpServletRequest request,@PathVariable ("email") String email ){
        return ResponseEntity.ok(
            ResponseUtil.sucess(
                uservice.obtenerUsuarioporEmail(email),
                "El usuario con el email solicitado",
                request.getRequestURI()));
     }

     //Eliminar
     @PreAuthorize("hasRole('ROLE_PRO')")
     @DeleteMapping("/delete/{id}")
     public ResponseEntity<ApiResponse<String>>eliminarUsuario(@PathVariable ("id") Integer id, HttpServletRequest request){
        return ResponseEntity.ok(
            ResponseUtil.sucess(
                uservice.eliminarUsuario(id),
                "Eliminar usuario introducido",
                request.getRequestURI()));
     }

     //InsertardesdeAdmin
     @PreAuthorize("hasRole('ROLE_PRO')")
     @PostMapping("/insert/user")
     public ResponseEntity<ApiResponse<UserDto>>insertarUsuariobyAdmin(@RequestBody User usuario, HttpServletRequest request){
        return ResponseEntity.ok(
            ResponseUtil.sucess(
                uservice.insertarUsuario(usuario),
                "Se ha insertado el usuario correctamente por Admin",
                request.getRequestURI())
        );

     }




     //Actualizar usuario.
     @PreAuthorize("hasRole('ROLE_PRO')")
     @PutMapping("/update")
     public ResponseEntity<ApiResponse<UserDto>>actualizarUsuario(@RequestBody User usuario, HttpServletRequest request){
        return ResponseEntity.ok(
            ResponseUtil.sucess(
                uservice.updateUsuaiDto(usuario),
                "Usuario ha sido actualizado",
                request.getRequestURI())
        );
     }
     //Actualizarme solo a mis datos.


     //Actualizar la contraseña: solo puede cambiar la suya propia.
     @PostMapping("/updatecontrasena")
     public ResponseEntity<ApiResponse<Object>> updateContrasena(@RequestBody AuthModel modelo, HttpServletRequest request){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String emailAutenticado = auth.getName();
        if (!emailAutenticado.equals(modelo.getEmail())) {
            throw new org.springframework.security.access.AccessDeniedException(
                "No puedes cambiar la contraseña de otro usuario");
        }
        return ResponseEntity.ok(
            ResponseUtil.sucess(
                uservice.cambiarContrasenaUsuario(modelo),
                "La contraseña ha sido actualizada",
                request.getRequestURI())
        );
     }

     @PutMapping("/update/me")
     public ResponseEntity<ApiResponse<UserDto>>actualizarDatosUsuario(@RequestBody UserDto usuario,HttpServletRequest request){
        //Obtenemos el email logueadoº
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        //Actualizamos los datos del usuario logueado.
        return ResponseEntity.ok(
            ResponseUtil.sucess(
                uservice.actualizarUsuarioPropio(email,usuario),
                "Usuario logueado ha sido actualizado",
                request.getRequestURI())
        );
     }

     //Obtener los roles del usuario  logueado por email
     @GetMapping("/merole")
     public ResponseEntity<?>obtenerRolesUsuario(HttpServletRequest request){
        //obtenemos el email logueado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        UserDto userencontrado = uservice.obtenerUsuarioporEmail(email);
        
        return ResponseEntity.ok(
            Map.of(
                "roles", userencontrado.getRole(),
                "nombre", userencontrado.getNombre()
            )
        );
     }

     //Obtener datos mios
     @GetMapping("/infopropia")
     public ResponseEntity<?> obtenerDatosPropios(HttpServletRequest request){
        //Obtener el email
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return ResponseEntity.ok(
            ResponseUtil.sucess(
                uservice.obtenerDatosUsuario(email), 
                "Usuario propio con sus detalles",
                request.getRequestURI())
            );
     }
     

     }

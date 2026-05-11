package com.trabajofinal.ticketyap.auth;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trabajofinal.ticketyap.auth.service.AuthService;
import com.trabajofinal.ticketyap.modelos.AuthModel;
import com.trabajofinal.ticketyap.modelos.RecoverPassword;
import com.trabajofinal.ticketyap.modelos.User;
import com.trabajofinal.ticketyap.response.ApiResponse;
import com.trabajofinal.ticketyap.response.ResponseUtil;
import com.trabajofinal.ticketyap.security.CustomUserDetails;
import com.trabajofinal.ticketyap.service.CookieService;
import com.trabajofinal.ticketyap.service.RefreshTokenService;
import com.trabajofinal.ticketyap.utilitys.JWutils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authservice;
    private final CookieService cookieService;
    private final RefreshTokenService refreshTokenService;
    private final JWutils jutils;

    //Inyectamos el servicio que luego lo cambiamos por el userdetails

    public AuthController(AuthService authservice,CookieService cookieService,
        RefreshTokenService refreshTokenService,JWutils jutils){
        this.authservice = authservice;
        this.cookieService = cookieService;
        this.refreshTokenService = refreshTokenService;
        this.jutils =jutils;

    }

  

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Object>>confirmarlogin(@RequestBody AuthModel modelo , HttpServletRequest request, HttpServletResponse response){

        User usuarioEncontrado = authservice.login(modelo);

        //Encontrado el usuario.
        String tokenduradero = refreshTokenService.crearTokenRefresco(usuarioEncontrado.getId_usuario());
        System.out.println("El token duradero es: " + tokenduradero);

        //Hacemoos la conversion->
        CustomUserDetails usuario = new CustomUserDetails(usuarioEncontrado);
        String tokencreado = jutils.generateToken(usuario);

        cookieService.addHttpOnlyCookie("tokencreadorefresh", tokencreado, 15*60,response);
        cookieService.addHttpOnlyCookie("eternaltoken", tokenduradero, 30*24*60*60, response);

        System.out.println("Aqui esta el token" + tokencreado);
        System.out.println("Aqui esta el token duradero" + tokenduradero);

        return ResponseEntity.ok(
            ResponseUtil.sucess(
                "Se envian correctamente las cookies al front End",
                "La verificación con JWtoken esta ok",
                request.getRequestURI())
            );
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Object>>registro(@RequestBody User usuario, HttpServletRequest request){
        return ResponseEntity.ok(
            authservice.register(usuario, request)
        );
    }

    //Voy a comprobar el email para recuperar la contraseña
    @PostMapping("/compraremail")
    public ResponseEntity<ApiResponse<Object>>confirmarEmail(@RequestBody RecoverPassword emailPassword,HttpServletRequest request ){
        return ResponseEntity.ok(
            ResponseUtil.sucess(
                authservice.ConfirmarUsuarioporEmail(emailPassword.getEmail()),
                 "Comprobamos si el usuario esta en la base de datos",
                  request.getRequestURI())
        );
    }

    // Compruebo mi accesos cuando le paso la cookie y si tiene acceso con la cookie de la database

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<Object>> confirmamoscookie(@CookieValue(name="eternaltoken", required=false) String cookie, HttpServletRequest request, HttpServletResponse response){
        System.out.println("Cookie recibida: " + cookie); // Agrega esta línea para depurar
        return ResponseEntity.ok(
            ResponseUtil.sucess(
                authservice.refreshcookie(cookie, request, response),
                 "Comprobacion de la cookie de acceso",
                  request.getRequestURI())
            );
    }


    //Mandamos desde el front las credenciales del frontEnd
    @PostMapping("/loguin/google")
    public ResponseEntity<ApiResponse<Object>> loguinGoogle(@RequestBody Map<String,String> tokenMap, HttpServletRequest request, HttpServletResponse response){
        String token = tokenMap.get("credential");
        
        return ResponseEntity.ok(
            ResponseUtil.sucess(
                authservice.loguinGoogle(token, request, response),
                 "Loguin con google",
                  request.getRequestURI())
            );
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Object>> logout(HttpServletRequest request, HttpServletResponse response) {
        cookieService.deleteCookie("tokencreadorefresh", response);
        cookieService.deleteCookie("eternaltoken", response);
        return ResponseEntity.ok(
            ResponseUtil.sucess(
                "Sesión cerrada",
                "Logout exitoso",
                request.getRequestURI())
        );
    }

    //Para testear.

    @GetMapping("/test")
    public ResponseEntity<?> testUsuario(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Usuario autenticado: " + auth.getName());
        return ResponseEntity.ok("Test exitoso" + auth.getName());
    }

    @GetMapping("/auth/me")
    public ResponseEntity<?> getAuthenticatedUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
        return ResponseEntity.ok(
            Map.of(
                "email", user.getUsername(),
                "roles", user.getAuthorities())
        );
    }

    

    
    

}

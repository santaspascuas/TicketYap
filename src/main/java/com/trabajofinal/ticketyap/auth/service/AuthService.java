package com.trabajofinal.ticketyap.auth.service;

import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import com.trabajofinal.ticketyap.dao.UserDao;
import com.trabajofinal.ticketyap.exceptions.NotFoundException;
import com.trabajofinal.ticketyap.modelos.AuthModel;
import com.trabajofinal.ticketyap.modelos.GoogleUserData;
import com.trabajofinal.ticketyap.modelos.RefreshToken;
import com.trabajofinal.ticketyap.modelos.User;
import com.trabajofinal.ticketyap.response.ApiResponse;
import com.trabajofinal.ticketyap.response.ResponseUtil;
import com.trabajofinal.ticketyap.security.CustomUserDetails;
import org.springframework.context.ApplicationEventPublisher;

import com.trabajofinal.ticketyap.events.RegistroUsuarioEvent;
import com.trabajofinal.ticketyap.service.CookieService;
import com.trabajofinal.ticketyap.service.GoogleAuthService;
import com.trabajofinal.ticketyap.service.RefreshTokenService;
import com.trabajofinal.ticketyap.utilitys.JWutils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Service
public class AuthService{

    //Le inyecto para poder utilizar el dao que es la consulta a la base de datos.
    private final UserDao userdao;
    private final JWutils jutils;
    private final AuthenticationManager authenticationmanager;
    private final PasswordEncoder passwordEncoder;
    private final CookieService cookieService;
    private final ApplicationEventPublisher eventPublisher;
    private final RefreshTokenService refreshTokenService;
    private final GoogleAuthService googleservice;
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    public AuthService(UserDao userdao, JWutils jutils,
        AuthenticationManager authenticationmanager,
        PasswordEncoder passwordEncoder,
        CookieService cookieService,
        ApplicationEventPublisher eventPublisher,
        RefreshTokenService refreshTokenService,
        GoogleAuthService googleservice) {
        this.userdao = userdao;
        this.jutils = jutils;
        this.authenticationmanager = authenticationmanager;
        this.passwordEncoder = passwordEncoder;
        this.cookieService = cookieService;
        this.eventPublisher = eventPublisher;
        this.refreshTokenService = refreshTokenService;
        this.googleservice = googleservice;
    }

    public Optional<User> getinfoauth(String email) {
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("[{}] Inicio", method);
        log.debug("Se recibe la variable:{}", email);
        //Le paso el email para poder usar el spring security
        Optional<User> user = userdao.selectUserbyemail(email);
        if (user.isEmpty()) {
            log.warn("No existe el usuario con ese email");
            throw new IllegalStateException("El usuario con nombre " + email + " no existe.");
        }
        return user;
    }

    public Boolean ConfirmarUsuarioporEmail(String email){
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("[{}] Inicio", method);
        log.debug("Se recibe la variable:{}:", email);
        Boolean usuariconfirmado = userdao.selectUserbyemail(email).isPresent();
        return usuariconfirmado;
    }


    public User login(AuthModel authmodel){
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("[{}] Inicio", method);

        try {
            authenticationmanager.authenticate(
            new UsernamePasswordAuthenticationToken(authmodel.getEmail(),authmodel.getContrasena())
        );
        //Le decimos al authmanager que utilice un password autenticador para pasarle la contra y el correo para verificarlo
        User usuarioencontrado = getinfoauth(authmodel.getEmail()).orElseThrow(
        ()->new NotFoundException(String.format( "El usuario con email '%s' no se encuentra disponible", authmodel.getEmail())));
        System.out.println("El usuario encontrado y que ha pasado el filtro" + usuarioencontrado);
        //Ahora lo que hacemos es pasarle el usuario encontrado al customuserdetails que sacara la informacion para ver si es correcta.
        //Ahora generariamos el token.

        return usuarioencontrado;
         } catch (AuthenticationException e) {
            throw new NotFoundException(String.format( "La contraseña del usuario es incorrecta")); 
         }
    }

    //Todo registrarse.

    public ApiResponse<Object>register(User usuarioRegistrado, HttpServletRequest request){
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("[{}] Inicio", method);
        log.debug("Se recibe del frontal: email={}", usuarioRegistrado.getEmail());

        if(userdao.selectUserbyemail(usuarioRegistrado.getEmail()).isPresent()){
            log.warn("No existe el usuario:",usuarioRegistrado.getEmail());
         throw new NotFoundException(String.format( "El usuario con email '%s' ya esta creado! Registrarse con un nuevo usuario.",usuarioRegistrado.getEmail()));   
        }

        if(userdao.selectUsuariobyIban(usuarioRegistrado.getNumero_cuenta().trim()).isPresent()){
            log.warn("No existe el usuario con ese Iban:",usuarioRegistrado.getEmail());
            throw new NotFoundException(String.format( "Ya existe un usuario registrado con ese número de cuenta de Iban")); 
        }
        usuarioRegistrado.setContrasena(passwordEncoder.encode(usuarioRegistrado.getContrasena()));
        usuarioRegistrado.setRole("ROLE_USER");
        Integer usuarioregiustrado = userdao.insertarUsuario(usuarioRegistrado);
        if(usuarioregiustrado !=1){
            log.warn("Problema a la hora de insertar el usuario");
            throw new IllegalStateException("Ha ocurrido un error al registrar el usuario por duplicado");
        }
        eventPublisher.publishEvent(new RegistroUsuarioEvent(usuarioRegistrado.getEmail()));

        return ResponseUtil.sucess(
            usuarioRegistrado,
            "Uusuario creado con exito.",
            request.getRequestURI());
    }


    public ApiResponse<Object> refreshcookie(String cookie, HttpServletRequest request, HttpServletResponse response){
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("[{}] Inicio", method);
        log.debug("Se reciben las cookies:{}", cookie);
        //Es la cookie de 30 dias.
        RefreshToken refreshtokenvalidado = refreshTokenService.validarTokenRefresco(cookie);

        if(refreshtokenvalidado==null){
            log.warn("Token de refresco invalido");
            throw new IllegalStateException("El refresh token no es valido");
        }
        //Una vez devuelto el token. Sacamos un refresh nuevo

        Integer idusuario = refreshtokenvalidado.getUser_id();

        User encontrado = userdao.selectusuariobyid(idusuario).orElseThrow(
            ()->new NotFoundException(String.format( "El usuario con id '%s' no se encuentra disponible", idusuario))
        );
        CustomUserDetails usuario = new CustomUserDetails(encontrado);
        log.debug("El customdetailUser", usuario);
        
        String tokencreado = jutils.generateToken(usuario);
        log.debug("El tokencreado", tokencreado);
        cookieService.addHttpOnlyCookie("tokencreadorefresh", tokencreado, 15*60,response);

        return ResponseUtil.sucess(
            "Se envia correctamente el token de acceso refrescado",
            "La verificación del usuario mediante Jason Web Token refrescado",
            request.getRequestURI());
    }

    public User loguinGoogle(String token, HttpServletRequest request, HttpServletResponse response) {
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("[{}] Inicio", method);
        log.debug("El token de google recibido:", token);

        GoogleUserData usedata = googleservice.verificarGoogleToken(token);

        if(usedata == null){
            log.warn("Si la identificacion del usuario es nula");
            throw  new NotFoundException(String.format( "No se encuentra ningun usuario con esta identificacion"));
        }
        //Aqui pasamos el google-> a usuario normal.
        if(ConfirmarUsuarioporEmail(usedata.getEmail())){
            log.warn("Se busca al usuario en la database");
            User usuarioencontrado = getinfoauth(usedata.getEmail()).orElseThrow(
        ()->new NotFoundException(String.format( "El usuario con email '%s' no se encuentra disponible",usedata.getEmail())));
        return usuarioencontrado;
        }else{
            log.info("Entramos en la creación de un nuevo usuario de GOOGLE");
            String randomPassword = passwordEncoder.encode(UUID.randomUUID().toString());
            String cuenta = "GOOGLE_CUENTA_USUARIO";
            User nuevoGoogle = new User(usedata.getName(), usedata.getEmail(), randomPassword, cuenta);
            nuevoGoogle.setRole("ROLE_USER");
            log.debug("Usuario Google creado: email={}", nuevoGoogle.getEmail());
            Integer usuarioregiustrado = userdao.insertarUsuario(nuevoGoogle);
        if(usuarioregiustrado !=1){
            log.warn("No se ha actualizado bien en la database");
            throw new IllegalStateException("Ha ocurrido un error al registrar el usuario por duplicado");
        }
        
            return getinfoauth(usedata.getEmail()).orElseThrow(
                ()->new NotFoundException(String.format( "El usuario con email '%s' no se encuentra disponible", usedata.getEmail())));

    }

    }
    @GetMapping("/auth/test")
    public ResponseEntity<?> testUsuario(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("El usuario autenticado es: " + auth.getName());
        return ResponseEntity.ok("Usuario autenticado: " + auth.getName());
    }
    
}

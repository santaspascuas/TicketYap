package com.trabajofinal.ticketyap.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.trabajofinal.ticketyap.dao.UserDao;
import com.trabajofinal.ticketyap.modelos.User;
import com.trabajofinal.ticketyap.service.UserService;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserDao userDao;
    private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

    public CustomUserDetailsService(UserDao userDao){
        this.userDao = userDao;
    }

    //Utilizamos el dao para no entrar en el circulo de beans

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.debug("Intentando cargar usuario con email: {}", username);

        User user = userDao.selectUserbyemail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        log.debug("Usuario encontrado: {}", user.getEmail());
        log.debug("Contraseña de la base de datos: {}", user.getContrasena());

        return new CustomUserDetails(user);
    }
}

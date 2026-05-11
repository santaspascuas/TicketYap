package com.trabajofinal.ticketyap.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.trabajofinal.ticketyap.modelos.User;

public class CustomUserDetails  implements  UserDetails{

    //Como implementamos la clase userdetails. Queremos obtner el correo y la contraeña para poder usarlos como autenticadores.
    //Por ello inyectamos la clase usuario y obtenemos los gets para usarlos.

    private final User user;

    public CustomUserDetails(User user){
        this.user=user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //Esto es como decir que retornaremos una lista con los roles de cada usuario.
        return List.of(new SimpleGrantedAuthority(user.getRole()));
    }

    @Override
    public String getPassword() {
        //Retorna la contraseña que le damos cuando hacemos el uso de custom details. Es decir, cuando le pasamos la informacion por post
        return user.getContrasena();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

}

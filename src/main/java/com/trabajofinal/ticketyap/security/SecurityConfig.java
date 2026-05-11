package com.trabajofinal.ticketyap.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;




@Configuration
@EnableWebSecurity
public class SecurityConfig {
    //Es la clase de configuración de SpingSecuity. La cual vamos a utilizar para usar nuestros filtros de validación.
    //Activamos los enables para poder hacer cambios.

    private final CustomUserDetailsService customdetailservice;
    private final JWTAuthFilter jwtAuthFilter;

    //inyectar
    public SecurityConfig(CustomUserDetailsService customdetailservice, JWTAuthFilter jwtAuthFilter){
        this.customdetailservice=customdetailservice;
        this.jwtAuthFilter=jwtAuthFilter;
    }

    //Debemos de tener nuestro filtro personalizado o securityfilterchain donde pondremos la configuracion personalizada.
    @Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http.csrf(AbstractHttpConfigurer::disable)
        .cors(Customizer.withDefaults())
        .authorizeHttpRequests(auth -> auth

            // ------------------ PUBLIC ------------------
            .requestMatchers(
                "/auth/**",
                "/evento/all",
                "/evento/buscar/**",
                "/evento/localizacion/**",
                "/evento/allImagenes",
                "/entrada/buscarevento/**",
                "/localizacion/all",
                "/user/update/me",
                "/user/merole",
                "/entrada/all",
                "/entrada/buscar/**"
            ).permitAll()

            // ------------------ PUBLIC ------------------  // temporal para test
            .requestMatchers("/api/batch/**").permitAll()

            // ------------------ ADMIN ------------------
            .requestMatchers(
                "/evento/insert",
                "/evento/update",
                "/evento/delete/**",
                "/evento/subirImagen",
                "/localizacion/alta",
                "/localizacion/**",   // delete
                "/user/all",
                "/user/email/**",
                "/user/update",
                "/user/delete/**"
            ).hasRole("PRO")

            // ------------------ AUTHENTICATED ------------------
            .requestMatchers(
                "/sendata/**",
                "/entrada/**",   // ver entrada propia
                "/user/me/**",
                "/entrada/insert"
            ).authenticated()

            // ------------------ DEFAULT ------------------
            .anyRequest().authenticated()
        )
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authenticationProvider(authenticationProvider())
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
}


    @Bean
    public AuthenticationManager authenticationmanager(AuthenticationConfiguration authenticationconfiguration) throws Exception{
        //El authentication manager lo generamos a parte de un objeto llamado autenticador de configuracion que devuelve el objeto atrapado el authenticador
        return authenticationconfiguration.getAuthenticationManager();
    }
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        System.out.println("ME ESTOY EJECUTANDO---------------");
        daoAuthenticationProvider.setUserDetailsService(customdetailservice);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        System.out.println("DaoAuthenticationProvider configurado correctamente"); // 👈 log
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }




}

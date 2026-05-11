package com.trabajofinal.ticketyap.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.trabajofinal.ticketyap.utilitys.JWutils;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTAuthFilter  extends OncePerRequestFilter {

       private final JWutils jwutilis;
       private final CustomUserDetailsService customUserDetailsService;

    public JWTAuthFilter(JWutils jwUtils, CustomUserDetailsService customUserDetailsService) {
         this.customUserDetailsService=customUserDetailsService;
        this.jwutilis = jwUtils;
    }



   @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try {

            // 1. BUSCAR EL TOKEN EN LA COOKIE (no en header)
            String jwtToken = null;
            Cookie[] cookies = request.getCookies();

            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("tokencreadorefresh".equals(cookie.getName())) {
                        jwtToken = cookie.getValue();
                        break;
                    }
                }
            }
            //Nota mental: Recordar que si creas una cookie con un nombre no seas invecil y la llames de otro nombre. El ordeandor es listo y no tuy.
            

            // 2. Si no hay cookie → continuar (Spring Security decide si la ruta requiere auth)
            if (jwtToken == null || jwtToken.isBlank()) {
                filterChain.doFilter(request, response);
                return;
            }

            System.out.println("FILTRO JWT EJECUTÁNDOSE PARA:");

            // 3. Extraer email del token
            String userEmail = jwutilis.extractUsername(jwtToken);
            System.out.println("String userEmail = jwutilis.extractUsername(jwtToken)" + userEmail);

            // 4. Si hay email y aún no está autenticado
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = customUserDetailsService.loadUserByUsername(userEmail);

                // 5. Validar que el token sea válido para este usuario
                if (jwutilis.isValidToken(jwtToken, userDetails)) {

                    // 6. Crear autenticación y meterla en el contexto
                    UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                        );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    System.out.println("Usuario autenticado con cookie: {}" + userEmail );
                }
            }
        } catch (ExpiredJwtException ex) {
               response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("""
                    {
                        "success": false,
                        "message": "La sesión ha caducado",
                        "error": "TOKEN_EXPIRED",
                        "errorCode": 401
                    }
                """);
                return;
        }

        filterChain.doFilter(request, response);

}



    //Creado para decirle al filtro que no se aplique. 

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
         String path = request.getServletPath();

        return path.startsWith("/auth")
            || path.startsWith("/public")
            || path.equals("/");
    }



}



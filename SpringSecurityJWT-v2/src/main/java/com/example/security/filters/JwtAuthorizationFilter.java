package com.example.security.filters;


//-------------FILTROS------------------------
//filtro para cuando el usuario quiera acceder a nuestros endpoints

import com.example.model.service.UserDetailsServiceImpl;
import com.example.security.jwt.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component                                                                                                              //la podemos definir como componenet pq no le vamos a pasar ningun parametro adicional para que funcione.
public class JwtAuthorizationFilter extends OncePerRequestFilter {                                                      //OncePerRequestFilter : te obliga a authenticarte con to token cada vez q quieras acceder a un endpoint.

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,                                                //NonNull obliga a que estos parametros nunca puedan venir nulos.
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String tokenHeader = request.getHeader("Authorization");                                                              //extrae el token de la peticion.
        if(tokenHeader != null && tokenHeader.startsWith("Bearer ")){
            String token = tokenHeader.substring(7, tokenHeader.length());
            if(jwtUtils.IsTokenValid(token)){
                String username = jwtUtils.getUsernameFromToken(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, userDetails.getAuthorities());        //el null corresponde a la contraseña. no se necesita pasarla pq spring security la toma del userdetails

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);                              //este obj contiene la autheticacion propia en la aplicacion. cuando nos authenticamos, la authenticacion se guarda en el securityContextHolder.
            }
        }

        filterChain.doFilter(request, response);                                                                        // si no hay token o es invalido, va a continuar con el filtro de validacion. Y se va a dar cuenta por debajo que no tenemos autorizacion y va a denegar el acceso.

    }

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;
}

package com.example.security.filters;

import com.example.model.entity.UserEntity;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

//-------------FILTROS------------------------
//filtro para cuando el usuario se registra (autenticacion)
//filtro para cuando el usuario quiera acceder a nuestros endpoints

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        //recupera el usuario q se intenta autenticar.
        UserEntity userEntity = null;
        String username = "";
        String password = "";
        try{
            userEntity = new ObjectMapper().readValue(request.getInputStream(), UserEntity.class);                      //toma los parametros username y password y mapealos en un obj UserEntity.
            username = userEntity.getUsername();
            password = userEntity.getPassword();
        }catch (StreamReadException e){
            throw new RuntimeException(e);
        }catch (DatabindException e){
            throw new RuntimeException(e);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);            //con esto te autenticas en la app.

        return getAuthenticationManager().authenticate(authenticationToken);                                            //getAuthenticationManager() : el q se encarga de autenticar y le pasamos el obj authenticationToken.
    }


    //SI EN  attemptAuthentication() LA AUTENTICACION ES CORRECTA,  NOS PASAMOS AL  ---> successfulAuthentication()

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        1.38

        super.successfulAuthentication(request, response, chain, authResult);
    }
}

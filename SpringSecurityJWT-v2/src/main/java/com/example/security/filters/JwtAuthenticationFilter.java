package com.example.security.filters;

import com.example.model.entity.UserEntity;
import com.example.security.jwt.JwtUtils;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

//-------------FILTROS------------------------
//filtro para cuando el usuario se registra (autenticacion)

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    //como esta es una clase normal (no se usa @Component en esta clase), se tiene q usar un constructor para la inyeccion de dependecia.
    public JwtAuthenticationFilter(JwtUtils jwtUtils){
        this.jwtUtils = jwtUtils;
    }


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

        //si la autenticacion es correcta, se genera el token

        User user = (User) authResult.getPrincipal();                                                                   //obtienen los detalles del usuario con User de userdetails. getPrincipal() te trae los datos del usuario.
        String token = jwtUtils.generateAccessToken(user.getUsername());                                                //con esto ya estamos generando el token de acceso.
        response.addHeader("Authorization", token);                                                                     //una vez q se tiene el token, respondemos a la solicitud de acceso del usuario con el token generado.
        Map<String, Object> httpResponse = new HashMap<>();
        httpResponse.put("token", token);
        httpResponse.put("message", "Autenticacion correcta");
        httpResponse.put("username", user.getUsername());

        response.getWriter().write(new ObjectMapper().writeValueAsString(httpResponse));                                //escribimos el map en la respuesta. se convierte el map a json.
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().flush();                                                                                   //nos aseguramos de que tod0 se escriba correctamente en la respuesta.
        super.successfulAuthentication(request, response, chain, authResult);
    }



    private JwtUtils jwtUtils;
}

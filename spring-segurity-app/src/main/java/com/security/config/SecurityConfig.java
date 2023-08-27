package com.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    //CONFIGURACION 1
    /*@Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {                                //metodo q retorna un security filter chain.  httpSecurity : ya es un bean de spring security
        return httpSecurity
                //.csrf().disable()                                                                                     //min25 - csrf() : metodo para dar seguridad, evita que se pueda interceptar la comunicacion entre el browser y el servidor. lo q es una vulnerabilidad q se debe controlar.
                .authorizeHttpRequests()
                    .requestMatchers("/v1/index2").permitAll()
                    .anyRequest().authenticated()                                                                       //cualquier otra url necesita autenticacion.
                .and()
                    .formLogin().permitAll()
                .and()
                .build();
    }*/


    //CONFIGURACION 2 CON LAMBDA
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .authorizeHttpRequests( auth -> {
                    auth.requestMatchers("v1/index2").permitAll();
                    auth.anyRequest().authenticated();
                })
                .formLogin()
                    .successHandler(successHandler())                                                                   //en caso de exito en login, se define una pagina para redirigir.
                    .permitAll()
                .and()
                .sessionManagement()                                                                                    //sirve para configurar el comportamiento de las sessiones en spring security.
                    .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)                                                    //define la politica de creacion de seguridad. |ALWAYS| : va a crear una session siempre y cuando no exista ninguna.si hay una session existente, la va a reutilizar.  //  |IF_REQUIRED| : crea una nueva session solo si es necesario. si no hay session la crea,  y si ya hay una, la reutiliza.  //  |NEVER| : no crea ninguna session, pero si existe una, la va a usar.  // |STATELESS| : no crea ninguna session y no trabaja con sessiones.
                    .invalidSessionUrl("/login")                                                                            //si la session en invalida, por ej,  por no tener autenticacion, una session erronea, redirige al login.
                    .maximumSessions(1)                                                                                     //numero maximo de sessiones por usuario.
                    .expiredUrl("/login")                                                                                   //si el usuario esta inactivo 5 min, se redigige al login.
                    .sessionRegistry(sessionRegistry())                                                                     //ver datos del usuario en tiempo real. sessionRegistry() permite rastrear en tiempo real los datos del usuario al autenticarse.
                .and()
                .sessionFixation()                                                                                      //session Fixation es otra vulnerabilidad de las app web. un hacker puede usar esta vulnerabilidad y usar una session de manera indefinida. el hacker se apropia de un id de session y ataca a la app.
                    .migrateSession()                                                                                       //cuando se detecta un ataque de fijacion de session, spring va generar otro id de session.
                    //.newSession()                                                                                         //hace casi lo mismo q migrateSession,  pero newSession no copia los datos de la session, sino q crea una session completamente nueva.
                    //.none()                                                                                               //no recomendada. inhabilita la seguridad frente un ataque de fijacion de session.
                .and()
                //.httpBasic()                                                                                            //es una autenticacion basica. se tienen q mandar usuario y contraseña en el header de la peticion. sol se recomienda para app donde la seguridad no es tan importante.
                //.and()
                .build();
    }

    public AuthenticationSuccessHandler successHandler(){
        return ((request, response, authentication) -> {
           response.sendRedirect("/v1/session");
        });
    }

    @Bean
    public SessionRegistry sessionRegistry(){
        return new SessionRegistryImpl();
    }
}

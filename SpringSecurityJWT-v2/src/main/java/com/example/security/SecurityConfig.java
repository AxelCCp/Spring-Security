package com.example.security;

import com.example.security.filters.JwtAuthenticationFilter;
import com.example.security.filters.JwtAuthorizationFilter;
import com.example.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)                                                                      //quiere decir q vamos a habilitar las anotaciones para los controladores. en los controladores ahora puedes usar la anotacion @PreAuthorize.
public class SecurityConfig {

    //configurar cadena de filtros
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, AuthenticationManager authenticationManager) throws Exception {                  //hay que settearle un authentication manager. el profe lo puso manualemnte en los argumentos. esto se puede hacer,  pq AuthenticationManager ya esta definido como un bean en la clase.

        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtils);                                                        //necesitamos una instancia del filtro.
        jwtAuthenticationFilter.setAuthenticationManager(authenticationManager);
        jwtAuthenticationFilter.setFilterProcessesUrl("/login");                                                                                        //por defecto, la clase UsernamePasswordAuthenticationFilter de spring security, usa la ruta /login para autenticarse. aqui el profe le pone la misma ruta por defecto , aunq no es necesario ponerla.

        return httpSecurity
                .csrf(config -> config.disable())
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/hello").permitAll();
                    auth.anyRequest().authenticated();
                })
                .sessionManagement(session -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);                                     //no se va a manejar una session directamente.
                })
                //.httpBasic()                                                                                            //esta autenticacion basica será con un usuario en memoria.
                //.and()
                .addFilter(jwtAuthenticationFilter)
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)                    //esto quiere decir que el filtro q permite acceso a los diferentes endpoints se va a ejecutar antes que el filtro de autenticacion jwtAuthenticationFilter.
                .build();
    }

    /*
    @Bean
    UserDetailsService userDetailsService(){
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();                                          //sirve para crear un usuario en memoria.
        manager.createUser(User.withUsername("axelccp")
                .password("12345")
                .roles()                                                                                                //se settean  los roles,  pero no le pasamos nada.
                .build());

        return manager;
    }
    */

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(HttpSecurity httpSecurity, PasswordEncoder passwordEncoder) throws Exception {                      //administra la autenticacion del usuario q se le pasa.
        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)                                                               //se le envia el usuario en memoria q vamos a autenticar.
                .passwordEncoder(passwordEncoder)
                .and().build();
    }

    /*//EL PROFE USÓ ESTO PARA GENERAR UNA CONTRASEÑA ENCRIPTADA Y COPIARLA EN LA COLUMNA PASSWORD DE LA BASE DE DATOS,  TABLA USUARIOS. 1.56 HR.
    public static  void main(String[]args){
        System.out.println(new BCryptPasswordEncoder().encode("12345"));
    }
    */

    @Autowired
    public UserDetailsService userDetailsService;
    @Autowired
    public JwtUtils jwtUtils;
    @Autowired
    public JwtAuthorizationFilter jwtAuthorizationFilter;

}

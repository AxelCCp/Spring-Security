package com.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("v1")
public class CustomerController {

    @GetMapping("/index")
    public String index(){
        return "Hello";
    }

    @GetMapping("/index2")
    public String index2(){
        return "hello not secured";
    }

    @GetMapping("/session")
    public ResponseEntity<?>getDetailsSession(){

        String sessionId = "";
        User userObj = null;

        //se obtienen los datos de inicio de session del usuario.
        List<Object> sessions= sessionRegistry.getAllPrincipals();                                                      //devuelve una lista pq podemos tener muchas personas logueadas.
        for(Object session : sessions){
            if(session instanceof User){
                userObj = (User) session;                                                                               //informacion del usuario q se esta registrando
            }
            List<SessionInformation> sessionInformations = sessionRegistry.getAllSessions(session, false);               //con true obtienes todas las session,  expiradas y no expiradas. con FALSE se obtiene las sessiones activas de los usuarios.
            for(SessionInformation sessionInformation : sessionInformations){
                sessionId = sessionInformation.getSessionId();
            }
        }
        Map<String, Object> response = new HashMap<>();
        response.put("response","Hello world");
        response.put("sessionId", sessionId);
        response.put("sessionUser", userObj);

        return ResponseEntity.ok(response);
    }


    @Autowired
    private SessionRegistry sessionRegistry;

}

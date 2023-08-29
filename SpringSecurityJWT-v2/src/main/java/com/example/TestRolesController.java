package com.example;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestRolesController {

    @GetMapping("access-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String accessAdmin(){
        return "Hola, has accedido con rolde ADMIN";
    }

    @GetMapping("access-user")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")                //@PreAuthorize("hasAnyRole('USER','ADMIN')")
    public String accessUser(){
        return "Hola, has accedido con rolde USER";
    }

    @GetMapping("access-invited")
    @PreAuthorize("hasRole('INVITED')")
    public String accessInvited(){
        return "Hola, has accedido con rolde INVITED";
    }

}

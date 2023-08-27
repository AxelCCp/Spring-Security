package com.example.controller;

import com.example.controller.request.CreateUserDTO;
import com.example.model.entity.UserEntity;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PrincipalController {

    @GetMapping("/hello")
    public String hello(){
        return "hello not secured";
    }

    @GetMapping("/hello-secured")
    public String helloSecured(){
        return "hello secured";
    }

    @PostMapping("/create-user")
    public ResponseEntity<?>createUser(@Valid @RequestBody CreateUserDTO createUserDTO){
        UserEntity userEntity  = 
    }

}

package com.example.controller;

import com.example.controller.request.CreateUserDTO;
import com.example.model.dao.UserRepository;
import com.example.model.entity.ERole;
import com.example.model.entity.RoleEntity;
import com.example.model.entity.UserEntity;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
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

        Set<RoleEntity> roles = createUserDTO.getRoles().stream()
                .map(role -> RoleEntity.builder()
                        .name(ERole.valueOf(role))
                        .build())   
                        .collect(Collectors.toSet());

        UserEntity userEntity  = UserEntity.builder()
                .username(createUserDTO.getUsername())
                .password(passwordEncoder.encode(createUserDTO.getPassword()))
                .email(createUserDTO.getEmail())
                .roles(roles)
                .build();

        userRepository.save(userEntity);
        log.info(userEntity.toString());
        return ResponseEntity.ok(userEntity);
    }


    @DeleteMapping("/delete-user")
    public String deleteUser(@RequestParam String id){
        userRepository.deleteById(Long.parseLong(id));
        return "Se ha borrado el usuario con id: ".concat(id);
    }


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
}

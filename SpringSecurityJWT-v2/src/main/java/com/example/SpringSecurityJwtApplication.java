package com.example;

import com.example.model.dao.UserRepository;
import com.example.model.entity.ERole;
import com.example.model.entity.RoleEntity;
import com.example.model.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@SpringBootApplication
public class SpringSecurityJwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityJwtApplication.class, args);
	}

	//este metodo se ejecuta al iniciar la aplicacion. este metodo permite crear usuarios apenas se levante la aplicacion.
	@Bean
	CommandLineRunner init(){
		return args -> {
			UserEntity userEntity = UserEntity.builder()
					.email("zzz@zzz.com")
					.username("zzz")
					.password(passwordEncoder.encode("12345"))
					.roles(Set.of(RoleEntity.builder()
							.name(ERole.valueOf(ERole.ADMIN.name()))
							.build()))
					.build();
			userRepository.save(userEntity);

			UserEntity userEntity2 = UserEntity.builder()
					.email("boo@up.com")
					.username("boo")
					.password(passwordEncoder.encode("12345"))
					.roles(Set.of(RoleEntity.builder()
							.name(ERole.valueOf(ERole.USER.name()))
							.build()))
					.build();
			userRepository.save(userEntity2);

			UserEntity userEntity3 = UserEntity.builder()
					.email("puar@zzz.com")
					.username("puar")
					.password(passwordEncoder.encode("12345"))
					.roles(Set.of(RoleEntity.builder()
							.name(ERole.valueOf(ERole.INVITED.name()))
							.build()))
					.build();
			userRepository.save(userEntity3);
		};
	}

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

}

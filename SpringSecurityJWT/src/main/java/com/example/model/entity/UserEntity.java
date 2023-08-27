package com.example.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name="users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Email
    @NotBlank
    @Size(max=80)
    private String email;
    @NotBlank
    @Size(max=30)
    private String username;
    @NotBlank
    private String password;
    @ManyToMany(fetch = FetchType.EAGER, targetEntity = RoleEntity.class, cascade = CascadeType.PERSIST)                            //eager pq queremos q nos traiga todos los roles asociados al usuario de una sola vez. //targetEntity : establece la relacion con.  //cascade :con persist --> cuando se ingrese un usuario a la bbdd, se necesita de de una vez se inserten los roles. pero si el usuario se elimina, no podemos permitir q elimine los roles.
    @JoinTable(name="user_roles", joinColumns = @JoinColumn(name="user_id"), inverseJoinColumns = @JoinColumn(name="role_id"))      //nombre de la tabla intermedia de la relacion. y nombre de la llaves foraneas.
    private Set<RoleEntity> roles;                                                                                                  //usa se pq no permite roles repetidos.
}

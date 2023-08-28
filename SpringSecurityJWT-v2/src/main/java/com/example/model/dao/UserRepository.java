package com.example.model.dao;

import com.example.model.entity.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {

    //opcion 1
    Optional<UserEntity>findByUsername(String username);

    //opcion 2
    @Query("select u from UserEntity u where u.username = ?1")
    Optional<UserEntity>getName(String username);
}

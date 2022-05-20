package com.rtyapaev.micronaut.repository;

import com.rtyapaev.micronaut.model.entity.UserEntity;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.repository.CrudRepository;

import java.util.Optional;

@JdbcRepository
public interface UserRepository extends CrudRepository<UserEntity, Long> {
    Optional<UserEntity> find(String msisdn);

    UserEntity save(String msisdn, String password);
}

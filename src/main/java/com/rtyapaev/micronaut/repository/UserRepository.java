package com.rtyapaev.micronaut.repository;

import com.rtyapaev.micronaut.model.entity.UserEntity;
import io.micronaut.context.annotation.Executable;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {
    @Executable
    Optional<UserEntity> find(String msisdn);
}

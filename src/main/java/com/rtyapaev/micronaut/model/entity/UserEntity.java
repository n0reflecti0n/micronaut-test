package com.rtyapaev.micronaut.model.entity;

import io.micronaut.data.annotation.DateCreated;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users", schema = "micronaut_test")
public class UserEntity {
    @Id
    @GeneratedValue
    private Long id;

    private String msisdn;

    private String password;

    @DateCreated
    private LocalDateTime dateAdded;
}

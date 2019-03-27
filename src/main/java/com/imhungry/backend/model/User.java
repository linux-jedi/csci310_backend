package com.imhungry.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

/**
 * Created by calebthomas on 3/25/19.
 */
@Entity
@Data
@Table(name="users")
public class User extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;

    private String email;

    @JsonIgnore
    private String password;
}

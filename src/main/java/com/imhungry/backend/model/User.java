package com.imhungry.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.imhungry.backend.UserListsJsonWrapper;
import lombok.Data;
import org.hibernate.annotations.Type;

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
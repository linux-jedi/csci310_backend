package com.imhungry.backend.model;

import com.imhungry.backend.UserListsJsonWrapper;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Data
public class UserLists extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long userId;

    @Type(type = "jsonb")
    @Column(columnDefinition = "json")
    private UserListsJsonWrapper userListsJsonWrapper;
}


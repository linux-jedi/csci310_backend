package com.imhungry.backend.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name="ingredients")
public class Ingredient extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long userId;

    private Double quantity;

    private String ingredientValue;

}

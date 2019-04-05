package com.imhungry.backend.repository;

import com.imhungry.backend.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    Collection<Ingredient> findAllByUserId(long userId);

    void deleteById(long id);

}

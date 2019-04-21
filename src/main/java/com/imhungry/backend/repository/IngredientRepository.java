package com.imhungry.backend.repository;

import com.imhungry.backend.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    Collection<Ingredient> findAllByUserId(long userId);

    Optional<Ingredient> findFirstByUserIdAndIngredientValue(long userId, String ingredientValue);

    Optional<Ingredient> findFirstById(long ingredientId);

}

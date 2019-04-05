package com.imhungry.backend.controller;

import com.imhungry.backend.model.Ingredient;
import com.imhungry.backend.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/grocery")
public class GroceryListController {

    @Autowired
    IngredientRepository ingredientRepository;

    @GetMapping
    public List<Ingredient> getGroceryList(@RequestParam(value = "userid") String userId) {
        long userIdLong = -1;
        try {
            userIdLong = Long.parseLong(userId);
        } catch (Exception e) {
        }
        return (List<Ingredient>) ingredientRepository.findAllByUserId(userIdLong);
    }

    @PostMapping("/addItem")
    public void addIngredient(@RequestParam(value = "userid") String userId,
                              @RequestBody String ingredient) {
        long userIdLong = -1;
        try {
            userIdLong = Long.parseLong(userId);
        } catch (Exception e) {
        }
        addIngredient(ingredient, userIdLong);
    }

    @PostMapping("/addItems")
    public void addIngredient(@RequestParam(value = "userid") String userId,
                              @RequestBody List<String> ingredients) {
        Long userIdLong = Long.parseLong(userId);
        for (String ingredient: ingredients) {
            addIngredient(ingredient, userIdLong);
        }
    }

    @DeleteMapping("/deleteItem")
    public void deleteItem(@RequestParam(value = "userid") String userId,
                           @RequestParam(value = "ingredientid") String ingredientId) {
        deleteIngredient(userId, Long.parseLong( ingredientId));
    }

    private void addIngredient(@RequestBody String ingredient, Long userIdLong) {
        Ingredient newIngredient = new Ingredient();
        newIngredient.setIngredientValue(ingredient);
        newIngredient.setUserId(userIdLong);
        ingredientRepository.saveAndFlush(newIngredient);
    }

    private void deleteIngredient(String userId, Long ingredientId) {
        Long userIdLong = Long.parseLong(userId);
        if (ingredientBelongsToUser(ingredientId, userIdLong))
         ingredientRepository.deleteById(ingredientId);
    }

    private boolean ingredientBelongsToUser(Long ingredientId, Long userId) {
        Optional<Ingredient> ingredient = ingredientRepository.findById(ingredientId);
        return ingredient.map(ingredient1 -> ingredient1.getUserId().equals(userId)).orElse(false);
    }
}

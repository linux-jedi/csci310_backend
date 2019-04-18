package com.imhungry.backend.controller;

import com.imhungry.backend.model.Ingredient;
import com.imhungry.backend.repository.IngredientRepository;
import com.imhungry.backend.sourcer.IngredientParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static java.lang.Long.parseLong;

@RestController
@CrossOrigin
@RequestMapping("/grocery")
public class GroceryListController {

    @Autowired
    IngredientRepository ingredientRepository;

    @GetMapping
    public List<Ingredient> getGroceryList(@RequestParam(value = "userid") String userId) {

        long userIdLong = parseLong(userId);
        return (List<Ingredient>) ingredientRepository.findAllByUserId(userIdLong);
    }

    @PostMapping("/addItem")
    public void postAddIngredient(@RequestParam(value = "userid") String userId,
                              @RequestBody String ingredient) {
        long userIdLong = parseLong(userId);
        addIngredient(ingredient, userIdLong);
    }

    @PostMapping("/addItems")
    public void postAddIngredients(@RequestParam(value = "userid") String userId,
                              @RequestBody List<String> ingredients) {
        Long userIdLong = parseLong(userId);
        for (String ingredient: ingredients) {
            addIngredient(ingredient, userIdLong);
        }
    }

    @DeleteMapping("/deleteItem")
    public void deleteItem(@RequestParam(value = "userid") String userId,
                           @RequestParam(value = "ingredientid") String ingredientId) {
        deleteIngredient(userId, parseLong( ingredientId));
    }

    public void addIngredient(String ingredient, Long userIdLong) {
        IngredientParser ingredientParser = new IngredientParser(ingredient);

        Optional<Ingredient> returnedIngredient = ingredientRepository.
                findFirstByUserIdAndIngredientValue(userIdLong,
                        ingredientParser.getIngredientValue());

        Ingredient ig = null;
        if (returnedIngredient.isPresent()) {
            ig = returnedIngredient.get();
            ingredientRepository.delete(ig);
        }

        Ingredient newIngredient = new Ingredient();
        newIngredient.setIngredientValue(ingredientParser.getIngredientValue());
        newIngredient.setQuantity(ingredientParser.getQuantity());
        newIngredient.setUserId(userIdLong);

        IngredientParser.collateIngredients(newIngredient, ig);
        ingredientRepository.saveAndFlush(newIngredient);
    }

    private void deleteIngredient(String userId, Long ingredientId) {
        Long userIdLong = parseLong(userId);
        if (ingredientBelongsToUser(ingredientId, userIdLong))
         ingredientRepository.deleteById(ingredientId);
    }

    private boolean ingredientBelongsToUser(Long ingredientId, Long userId) {
        Optional<Ingredient> ingredient = ingredientRepository.findById(ingredientId);
        return ingredient.map(ingredient1 -> ingredient1.getUserId().equals(userId)).orElse(false);
    }
}

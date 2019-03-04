package com.imhungry.backend;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by calebthomas on 2/22/19.
 */

@RestController
@RequestMapping("/recipe")
public class RecipeController {

    @GetMapping
    List<Recipe> recipeSearch(@RequestParam(value = "name", defaultValue = "Chinese") String keyword,
                              @RequestParam(value = "amount", defaultValue = "5") String amount) {
        List<Recipe> recipes = new ArrayList<>();
        int maxRecipes = Integer.valueOf(amount);


        return RecipeSourcer.getRecipes(keyword, maxRecipes);
    }

    @GetMapping(value = "/{recipeId}")
    Recipe getRecipe(@PathVariable String recipeId) {
        return RecipeSourcer.getRecipe(recipeId);
    }
}

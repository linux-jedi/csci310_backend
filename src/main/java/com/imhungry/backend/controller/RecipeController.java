package com.imhungry.backend.controller;

import com.imhungry.backend.Recipe;
import com.imhungry.backend.RecipeSourcer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by calebthomas on 2/22/19.
 */
@RestController
@CrossOrigin
@RequestMapping("/recipe")

public class RecipeController {

    @Autowired
    private RecipeSourcer recipeSourcer;

    @Autowired
    private ListManager listManager;

    @GetMapping
    List<Recipe> recipeSearch(@RequestParam(value = "name", defaultValue = "Chinese") String keyword,
                              @RequestParam(value = "amount", defaultValue = "5") String amount) throws Exception {

        int maxRecipes = Integer.valueOf(amount);

        List<Recipe> unsortedRecipes = recipeSourcer.getRecipes(keyword, maxRecipes);
        return listManager.filterSortRecipeList(unsortedRecipes);
    }

    @GetMapping(value = "/{recipeId}")
    Recipe getRecipe(@PathVariable String recipeId) throws Exception {
        return recipeSourcer.getRecipe(recipeId);
    }
}

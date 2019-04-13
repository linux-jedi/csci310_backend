package com.imhungry.backend.controller;

import com.imhungry.backend.data.Recipe;
import com.imhungry.backend.sourcer.RecipeSourcer;
import com.imhungry.backend.utils.UserListsJsonWrapper;
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
    private UserListsJsonWrapper userListsJsonWrapper;

    @GetMapping
    List<Recipe> recipeSearch(@RequestParam(value = "name", defaultValue = "chinese") String keyword,
                              @RequestParam(value = "amount", defaultValue = "5") String amount) throws Exception {

        int maxRecipes = Integer.valueOf(amount);

        List<Recipe> unsortedRecipes = recipeSourcer.searchRecipes(keyword, maxRecipes);
        return userListsJsonWrapper.filterSortRecipeList(unsortedRecipes);
    }

    @GetMapping(value = "/{recipeId}")
    Recipe getRecipe(@PathVariable String recipeId) throws Exception {
        return recipeSourcer.getRecipe(recipeId);
    }
}

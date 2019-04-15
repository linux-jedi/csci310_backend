package com.imhungry.backend.controller;

import com.imhungry.backend.data.Recipe;
import com.imhungry.backend.model.UserLists;
import com.imhungry.backend.repository.UserListsRepository;
import com.imhungry.backend.sourcer.RecipeSourcer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/recipe")

public class RecipeController {

    @Autowired
    private RecipeSourcer recipeSourcer;

    @Autowired
    private UserListsRepository userListsRepository;

    @GetMapping
    List<Recipe> recipeSearch(@RequestParam(value = "name", defaultValue = "chinese") String keyword,
                              @RequestParam(value = "amount", defaultValue = "5") String amount,
                              @RequestParam(value = "userid") String userid) throws Exception {

        int maxRecipes = Integer.valueOf(amount);
        long userIdLong = Long.parseLong(userid);

        // Acquire filtered recipe search
        List<Recipe> unsortedRecipes = recipeSourcer.searchRecipes(keyword, maxRecipes);

        Optional<UserLists> ul = userListsRepository.findByUserId(userIdLong);
        return ul.get().getUserListsJsonWrapper().filterSortRecipeList(unsortedRecipes);

    }

    @GetMapping(value = "/{recipeId}")
    Recipe getRecipeDetails(@PathVariable String recipeId) throws Exception {
        return recipeSourcer.getRecipe(recipeId);
    }
}

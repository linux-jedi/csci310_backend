package com.imhungry.backend;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by calebthomas on 2/22/19.
 */

@RestController
public class RecipeController {

    @RequestMapping("/recipe")
    List<Recipe> recipeSearch(@RequestParam(value = "name", defaultValue = "Chinese") String keyword,
                              @RequestParam(value = "amount", defaultValue = "5") String amount) {
        List<Recipe> recipes = new ArrayList<>();
        int maxRecipes = Integer.valueOf(amount);


        return recipes;
    }
}

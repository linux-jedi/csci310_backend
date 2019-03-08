package com.imhungry.backend;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by calebthomas on 2/22/19.
 */

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HungryList {

    public static enum ListType {
        FAVORITE,
        EXPLORE,
        BLOCK
    }

    @NonNull
    private final String name;

    @NonNull
    private final List<Restaurant> restaurants;

    @NonNull
    private final List<Recipe> recipes;

    public HungryList(String name) {
        this.name = name;
        restaurants = new ArrayList<>();
        recipes = new ArrayList<>();
    }

    public void addRestaurant(Restaurant restaurant) {
        restaurants.add(restaurant);
    }

    public void removeRestaurant(String restaurantId) {
        Predicate<Restaurant> isMatch = restaurant -> restaurant.getId().equals(restaurantId);

        restaurants.removeIf(isMatch);
    }

    public void addRecipe(Recipe recipe) {
        recipes.add(recipe);
    }

    public void removeRecipe(String recipeId) {
        Predicate<Recipe> isMatch = recipe -> recipe.getId().equals(recipeId);

        recipes.removeIf(isMatch);
    }
}

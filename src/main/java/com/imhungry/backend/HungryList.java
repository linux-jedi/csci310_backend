package com.imhungry.backend;

import lombok.Data;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by calebthomas on 2/22/19.
 */

@Data
public class HungryList {

    public enum ListType {
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

    public void removeRestaurant(int restaurantId) {
        Predicate<Restaurant> isMatch = restaurant -> restaurant.getId().equals(restaurantId);

        restaurants.removeIf(isMatch);
    }

    public void addRecipe(Recipe recipe) {
        recipes.add(recipe);
    }

    public void removeRecipe(int recipeId) {
        Predicate<Recipe> isMatch = recipe -> recipe.getId().equals(recipeId);

        recipes.removeIf(isMatch);
    }
}

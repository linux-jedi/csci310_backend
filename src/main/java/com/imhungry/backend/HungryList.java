package com.imhungry.backend;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
    private final List<ListItem> items;

    HungryList(String name) {
        this.name = name;
        items = new ArrayList<>();
    }

    List<Restaurant> getRestaurants() {
        List<Restaurant> res = items.stream()
                .filter(item -> item.getClass().equals(Restaurant.class))
                .map(item -> (Restaurant) item)
                .collect(Collectors.toList());
        return res;
    }

    List<Recipe> getRecipes() {
        List<Recipe> res = items.stream()
                .filter(item -> item.getClass().equals(Recipe.class))
                .map(item -> (Recipe) item)
                .collect(Collectors.toList());
        return res;
    }

    public void addItem(ListItem item) {
        if (!items.contains(item))
            items.add(item);
    }

    public void removeRestaurant(String restaurantId) {
        Predicate<ListItem> isMatch = restaurant -> restaurant.getId().equals(restaurantId);

        items.removeIf(isMatch);
    }

    public void removeRecipe(String recipeId) {
        Predicate<ListItem> isMatch = recipe -> recipe.getId().equals(recipeId);

        items.removeIf(isMatch);
    }
}

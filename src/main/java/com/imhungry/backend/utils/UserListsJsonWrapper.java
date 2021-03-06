package com.imhungry.backend.utils;

import com.imhungry.backend.data.HungryList;
import com.imhungry.backend.data.Recipe;
import com.imhungry.backend.data.Restaurant;
import lombok.Getter;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserListsJsonWrapper {

    @Getter
    private List<HungryList> hungryLists;

    public UserListsJsonWrapper() {
        hungryLists = new ArrayList<>();
        hungryLists.add(new HungryList(HungryList.ListType.FAVORITE.toString()));
        hungryLists.add(new HungryList(HungryList.ListType.EXPLORE.toString()));
        hungryLists.add(new HungryList(HungryList.ListType.BLOCK.toString()));
    }

    public List<Restaurant> filterSortRestaurantList(List<Restaurant> originalList) {
        List<Restaurant> restaurants = new ArrayList<>();
        List<Restaurant> favoritesRestaurants = hungryLists.get(0).getRestaurants();
        List<Restaurant> blockRestaurants = hungryLists.get(2).getRestaurants();

        for (Restaurant searchRestaurant : originalList) {
            boolean added = false;

            for (Restaurant favoritesRestaurant : favoritesRestaurants) {
                if (favoritesRestaurant.getId().equals(searchRestaurant.getId())) {
                    restaurants.add(0, searchRestaurant);
                    added = true;
                }
            }

            for (Restaurant blockRestaurant: blockRestaurants) {
                if (blockRestaurant.getId().equals(searchRestaurant.getId())) {
                    added = true;
                }
            }

            if (!added) {
                restaurants.add(searchRestaurant);
            }
        }

        return restaurants;
    }


    public List<Recipe> filterSortRecipeList(List<Recipe> originalList) {
        List<Recipe> recipes = new ArrayList<>();
        List<Recipe> favoritesRecipes = hungryLists.get(0).getRecipes();
        List<Recipe> blockRecipes = hungryLists.get(2).getRecipes();

        for (Recipe searchRecipe : originalList) {
            boolean added = false;

            for (Recipe favoritesRecipe : favoritesRecipes) {
                if (favoritesRecipe.getId().equals(searchRecipe.getId())) {
                    recipes.add(0, searchRecipe);
                    added = true;
                }
            }

            for (Recipe blockRecipe: blockRecipes) {
                if (blockRecipe.getId().equals(searchRecipe.getId())) {
                    added = true;
                }
            }

            if (!added) {
                recipes.add(searchRecipe);
            }
        }

        return recipes;
    }
}
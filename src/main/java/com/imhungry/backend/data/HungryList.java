package com.imhungry.backend.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;


@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HungryList {

    public enum ListType {
        FAVORITE,
        EXPLORE,
        BLOCK
    }

    @NonNull
    private final String name;

    @NonNull
    private final List<ListItem> items;

    public HungryList(String name) {
        this.name = name;
        items = new ArrayList<>();
    }

    public List<Restaurant> getRestaurants() {
        return items.stream()
                .filter(item -> item.getClass().equals(Restaurant.class))
                .map(item -> (Restaurant) item)
                .collect(Collectors.toList());
    }

    public List<Recipe> getRecipes() {
        return items.stream()
                .filter(item -> item.getClass().equals(Recipe.class))
                .map(item -> (Recipe) item)
                .collect(Collectors.toList());
    }

    public void addItem(ListItem item) {
        if (!items.contains(item))
            items.add(item);
    }

    public void removeItem(String itemId) {
        Predicate<ListItem> isMatch = item -> item.getId().equals(itemId);
        items.removeIf(isMatch);
    }
}

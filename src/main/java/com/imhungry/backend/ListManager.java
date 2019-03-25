package com.imhungry.backend;

import lombok.Getter;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by calebthomas on 3/1/19.
 */
@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ListManager {

    @Getter
    private List<HungryList> hungryLists;

    public ListManager() {
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


}

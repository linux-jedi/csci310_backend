package com.imhungry.backend.controller;

/**
 * Created by calebthomas on 2/22/19.
 */

import com.imhungry.backend.UserListsJsonWrapper;
import com.imhungry.backend.Restaurant;
import com.imhungry.backend.RestaurantSourcer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/restaurant")
public class RestaurantController {

    @Autowired
    private RestaurantSourcer restaurantSourcer;

    @Autowired
    private UserListsJsonWrapper userListsJsonWrapper;

    @GetMapping
    public List<Restaurant> restaurantSearch(@RequestParam(value="name", defaultValue="Chinese") String keyword,
                                             @RequestParam(value="amount", defaultValue="5") String amount,
                                             HttpSession session) throws Exception {

        // Limit number of results requested
        int maxRestaurants = Integer.valueOf(amount);
        if(maxRestaurants > 100) {
            maxRestaurants = 100;
        }

        List<Restaurant> unsortedRestaurants = restaurantSourcer.searchRestaurants(keyword, maxRestaurants);

        return userListsJsonWrapper.filterSortRestaurantList(unsortedRestaurants);
    }

    @GetMapping(value = "/{placeId}")
    public Restaurant getRestaurant(@PathVariable String placeId) {
        return restaurantSourcer.getRestaurantDetails(placeId);
    }
}
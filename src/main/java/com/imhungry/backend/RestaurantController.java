package com.imhungry.backend;

/**
 * Created by calebthomas on 2/22/19.
 */

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/restaurant")
public class RestaurantController {

    @GetMapping
    public List<Restaurant> restaurantSearch(@RequestParam(value="name", defaultValue="Chinese") String keyword,
                                             @RequestParam(value="amount", defaultValue="5") String amount,
                                             HttpSession session) {
        // List of restaurants to return
        List<Restaurant> restaurants = new ArrayList<>();

        // Limit number of results requested
        int maxRestaurants = Integer.valueOf(amount);
        if(maxRestaurants > 100) {
            maxRestaurants = 100;
        }

        return RestaurantSourcer.searchRestaurants(keyword, maxRestaurants);
    }

    @GetMapping(value = "/{placeId}")
    public Restaurant getRestaurant(@PathVariable String placeId) {
        return RestaurantSourcer.getRestaurantDetails(placeId);
    }
}

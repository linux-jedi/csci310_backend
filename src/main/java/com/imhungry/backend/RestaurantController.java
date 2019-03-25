package com.imhungry.backend;

/**
 * Created by calebthomas on 2/22/19.
 */

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
    private ListManager listManager;

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

        return listManager.filterSortRestaurantList(unsortedRestaurants);
    }

    @GetMapping(value = "/{placeId}")
    public Restaurant getRestaurant(@PathVariable String placeId) {
        return restaurantSourcer.getRestaurantDetails(placeId);
    }
}
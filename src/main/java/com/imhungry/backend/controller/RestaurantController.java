package com.imhungry.backend.controller;

import com.imhungry.backend.data.Restaurant;
import com.imhungry.backend.model.UserLists;
import com.imhungry.backend.repository.UserListsRepository;
import com.imhungry.backend.sourcer.RestaurantSourcer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/restaurant")
public class RestaurantController {

    @Autowired
    private RestaurantSourcer restaurantSourcer;

    @Autowired
    private UserListsRepository userListsRepository;

    @GetMapping
    public List<Restaurant> restaurantSearch(@RequestParam(value="name", defaultValue="chinese") String keyword,
                                             @RequestParam(value="amount", defaultValue="5") String amount,
											 @RequestParam(value="radius", defaultValue="10000") String radius,
                                             @RequestParam(value = "userid") String userid) throws Exception {

        // Limit number of results requested
        int maxRestaurants = Integer.valueOf(amount);
        if(maxRestaurants > 100) {
            maxRestaurants = 100;
        }

        long userIdLong = Long.parseLong(userid);
        int rad = Integer.parseInt(radius);

        List<Restaurant> unsortedRestaurants = restaurantSourcer.searchRestaurants(keyword, maxRestaurants, rad);

        if (unsortedRestaurants != null) {
            Optional<UserLists> ul = userListsRepository.findByUserId(userIdLong);
            if (ul.isPresent())
                unsortedRestaurants = ul.get().getUserListsJsonWrapper().filterSortRestaurantList(unsortedRestaurants);
        }

        return new ArrayList<>();

    }

    @GetMapping(value = "/{placeId}")
    public Restaurant getRestaurant(@PathVariable String placeId) {
        return restaurantSourcer.getRestaurantDetails(placeId);
    }
}
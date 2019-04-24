package com.imhungry.backend.controller;

import com.imhungry.backend.data.Restaurant;
import com.imhungry.backend.exception.UserNotFoundException;
import com.imhungry.backend.model.UserLists;
import com.imhungry.backend.repository.UserListsRepository;
import com.imhungry.backend.sourcer.RestaurantSourcer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/restaurant")
public class RestaurantController {

    @NotNull
    private final RestaurantSourcer restaurantSourcer;

    @NotNull
    private final UserListsRepository userListsRepository;

    @Autowired
    public RestaurantController(RestaurantSourcer restaurantSourcer, UserListsRepository userListsRepository) {
        this.restaurantSourcer = restaurantSourcer;
        this.userListsRepository = userListsRepository;
    }

    @GetMapping
    public List<Restaurant> restaurantSearch(@RequestParam(value="name", defaultValue="chinese") String keyword,
                                             @RequestParam(value="amount", defaultValue="5") String amount,
											 @RequestParam(value="radius", defaultValue="3") String milesRadius,
                                             @RequestParam(value = "userid") String userid) throws Exception {

        long userIdLong = Long.parseLong(userid);

        // Limit number of results requested
        int maxRestaurants = Integer.min(100,  Integer.valueOf(amount));

        // Convert to meters for API call
        double milesRad = Double.parseDouble(milesRadius);
        int metersRadius = milesToMeters(milesRad);

        List<Restaurant> unsortedRestaurants = restaurantSourcer.searchRestaurants(keyword, maxRestaurants, metersRadius);

        Optional<UserLists> ul = userListsRepository.findByUserId(userIdLong);
        if (ul.isPresent())
            return ul.get().getUserListsJsonWrapper().filterSortRestaurantList(unsortedRestaurants);
        else
            throw new UserNotFoundException(userid);
    }

    public static int milesToMeters(double milesRad) {
        double METERS_IN_MILE = 1609.34;
        return (int) (milesRad * METERS_IN_MILE);
    }

    @GetMapping(value = "/{placeId}")
    public Restaurant getRestaurant(@PathVariable String placeId) {
        return restaurantSourcer.getRestaurantDetails(placeId);
    }
}
package com.imhungry.backend;

/**
 * Created by calebthomas on 2/22/19.
 */
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.*;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;
import com.google.maps.model.RankBy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RestController
public class RestaurantController {

    @RequestMapping("/restaurant")
    public List<Restaurant> restaurantSearch(@RequestParam(value="name", defaultValue="Chinese") String keyword,
                                             @RequestParam(value="amount", defaultValue="5") String amount,
                                             HttpSession session) {
        System.out.println(session.getId());

        // List of restaurants to return
        List<Restaurant> restaurants = new ArrayList<>();
        int maxRestaurants = Integer.valueOf(amount);

        // Get all restaurants from google API
        // TODO: create better method for storing API key
        LatLng tommy = new LatLng(34.020633, -118.285468);
        GeoApiContext geoApiContext = new GeoApiContext.Builder()
                .apiKey("API KEY HERE")
                .build();

        NearbySearchRequest req = new NearbySearchRequest(geoApiContext);
        req.location(tommy)
                .keyword(keyword)
                .rankby(RankBy.DISTANCE)
                .custom("type", "restaurant");

        PlacesSearchResponse response = req.awaitIgnoreError();

        // TODO: implement paging for maxAmount over 20
        PlacesSearchResult[] results = response.results;

        for(int i = 0; i < results.length && i <maxRestaurants; i++ ) {
            restaurants.add(new Restaurant(
                    results[i].placeId,
                    results[i].name,
                    results[i].formattedAddress,
                    "Phone number",
                    results[i].vicinity
            ));
        }

        return restaurants;
    }
}

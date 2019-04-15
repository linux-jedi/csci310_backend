package com.imhungry.backend.sourcer;

import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.NearbySearchRequest;
import com.google.maps.PlaceDetailsRequest;
import com.google.maps.model.*;
import com.imhungry.backend.data.Restaurant;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.sort;


public class RestaurantSourcer {

    @Value("${gplaces.api.key}")
    private String API_KEY;

    public List<Restaurant> searchRestaurants(String keyword, int maxRestaurants, int radius) throws Exception {
        List<Restaurant> restaurants = new ArrayList<>();

        // Get all restaurants from google API
        LatLng tommy = new LatLng(34.020633, -118.285468);
        GeoApiContext geoApiContext = new GeoApiContext.Builder()
                .apiKey(API_KEY)
                .build();

        NearbySearchRequest req = new NearbySearchRequest(geoApiContext);

        req.location(tommy)
                .keyword(keyword)
                .radius(radius)
                .type(PlaceType.RESTAURANT);
        PlacesSearchResponse response = req.awaitIgnoreError();

        PlacesSearchResult[] placesSearchResults = response.results;
        int resultsIndex = 0;
        int i = 0;
        while(i < maxRestaurants && resultsIndex < placesSearchResults.length) {

            // Get distance from tommy trojan to the restaurant
            DistanceMatrixApiRequest distanceRequest = new DistanceMatrixApiRequest(geoApiContext);
            distanceRequest.origins(tommy).destinations(placesSearchResults[resultsIndex].vicinity);
            DistanceMatrix distanceResponse = null;
            distanceResponse = distanceRequest.await();


            // Get detailed information about the location
            PlaceDetailsRequest placeDetailsRequest = new PlaceDetailsRequest(geoApiContext);
            placeDetailsRequest.placeId(placesSearchResults[resultsIndex].placeId);
            PlaceDetails placeDetails = null;

            placeDetails = placeDetailsRequest.await();

            restaurants.add(new Restaurant(
                    placesSearchResults[resultsIndex].placeId,
                    placesSearchResults[resultsIndex].name,
                    placesSearchResults[resultsIndex].vicinity,
                    placeDetails.formattedPhoneNumber,
                    placeDetails.website,
                    placesSearchResults[resultsIndex].rating,
                    placeDetails.priceLevel,
                    distanceResponse.rows[0].elements[0].duration.humanReadable,
                    (int) distanceResponse.rows[0].elements[0].duration.inSeconds
            ));

            // iterate
            i++;
            resultsIndex++;

            // Get next page
            if(resultsIndex == placesSearchResults.length) {

                req = new NearbySearchRequest(geoApiContext);
                req.pageToken(response.nextPageToken);
                placesSearchResults = req.awaitIgnoreError().results;
                resultsIndex = 0;
            }
        }

        sort(restaurants);
        return restaurants;
    }

    public Restaurant getRestaurantDetails(String placeId) {
        GeoApiContext geoApiContext = new GeoApiContext.Builder()
                .apiKey(API_KEY)
                .build();

        PlaceDetailsRequest req = new PlaceDetailsRequest(geoApiContext);
        req.placeId(placeId);

        PlaceDetails place = req.awaitIgnoreError();

        return new Restaurant(
                placeId,
                place.name,
                place.formattedAddress,
                place.formattedPhoneNumber,
                place.website,
                place.rating,
                place.priceLevel,
                "Placeholder distance",
                0
        );
    }
}

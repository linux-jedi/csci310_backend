package com.imhungry.backend;

import okhttp3.HttpUrl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Created by calebthomas on 3/7/19.
 */

@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = "dev")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestaurantControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private RestaurantSourcer restaurantSourcer;

    @Test
    public void testRestaurantSearch() throws Exception {

        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host("localhost")
                .port(port)
                .addPathSegment("restaurant")
                .addQueryParameter("name", "burger")
                .addQueryParameter("amount", "5")
                .addQueryParameter("radius", "10000")
                .build();

        ResponseEntity<Restaurant[]> entity = restTemplate.getForEntity(url.toString(), Restaurant[].class);
        Restaurant[] restaurants = entity.getBody();

        assertEquals(restaurants.length, 5);
        int prev = 0;
        for (Restaurant restaurant: restaurants) {
            assert (restaurant.getTime() >= prev);
            prev = restaurant.getTime();
        }

        url = new HttpUrl.Builder()
                .scheme("http")
                .host("localhost")
                .port(port)
                .addPathSegment("restaurant")
                .addQueryParameter("name", "burger")
                .addQueryParameter("amount", "30")
                .addQueryParameter("radius", "10000")
                .build();

        entity = restTemplate.getForEntity(url.toString(), Restaurant[].class);
        restaurants = entity.getBody();

        assertEquals(restaurants.length, 30);
    }

    @Test
    public void testLargeRestaurantSearch() throws Exception {

        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host("localhost")
                .port(port)
                .addPathSegment("restaurant")
                .addQueryParameter("name", "burger")
                .addQueryParameter("amount", "101")
                .addQueryParameter("radius", "10000")
                .build();

        ResponseEntity<Restaurant[]> entity = restTemplate.getForEntity(url.toString(), Restaurant[].class);
        Restaurant[] restaurants = entity.getBody();

        int prev = 0;
        for (Restaurant restaurant: restaurants) {
            assert (restaurant.getTime() >= prev);
            prev = restaurant.getTime();
        }
    }

    @Test
    public void testGetRestaurantDetails() throws IOException {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host("localhost")
                .port(port)
                .addPathSegment("restaurant")
                .addPathSegment("ChIJW-yJPuPHwoARGh0NU_IeYpI")
                .build();

        ResponseEntity<Restaurant> entity = restTemplate.getForEntity(url.toString(), Restaurant.class);
        Restaurant r = entity.getBody();

        assertEquals(r.getId(), "ChIJW-yJPuPHwoARGh0NU_IeYpI");
    }
}

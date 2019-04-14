package com.imhungry.backend;

import com.imhungry.backend.data.Restaurant;
import com.imhungry.backend.sourcer.RestaurantSourcer;
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

import static org.junit.Assert.assertEquals;


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
    public void testRestaurantSearch() {

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
    }

    @Test
    public void testTrueRestaurantSearch() {

        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host("localhost")
                .port(port)
                .addPathSegment("restaurant")
                .addQueryParameter("name", "burger")
                .addQueryParameter("amount", "2")
                .addQueryParameter("radius", "10000")
                .build();

        ResponseEntity<Restaurant[]> entity = restTemplate.getForEntity(url.toString(), Restaurant[].class);
        Restaurant[] restaurants = entity.getBody();

        assertEquals(restaurants.length, 2);
    }

    @Test
    public void testGetRestaurantDetails() {
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

    @Test
    public void testTrueGetRestaurantDetails() {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host("localhost")
                .port(port)
                .addPathSegment("restaurant")
                .addPathSegment("ChIJdzrHbse4woARwbth0qmfStw")
                .build();

        ResponseEntity<Restaurant> entity = restTemplate.getForEntity(url.toString(), Restaurant.class);
        Restaurant r = entity.getBody();

        assertEquals(r.getId(), "ChIJdzrHbse4woARwbth0qmfStw");
    }

}

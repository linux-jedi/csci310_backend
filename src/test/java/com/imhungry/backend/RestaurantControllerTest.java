package com.imhungry.backend;

import okhttp3.HttpUrl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Created by calebthomas on 3/7/19.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestaurantControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testRestaurantSearch() {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host("localhost")
                .port(port)
                .addPathSegment("restaurant")
                .addQueryParameter("name", "chinese")
                .addQueryParameter("amount", "5")
                .build();

        ResponseEntity<Restaurant[]> entity = restTemplate.getForEntity(url.toString(), Restaurant[].class);
        Restaurant[] restaurants = entity.getBody();

        assertEquals(restaurants.length, 5);
    }

    @Test
    public void testLargeRestaurantSearch() {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host("localhost")
                .port(port)
                .addPathSegment("restaurant")
                .addQueryParameter("name", "chinese")
                .addQueryParameter("amount", "15")
                .build();

        ResponseEntity<Restaurant[]> entity = restTemplate.getForEntity(url.toString(), Restaurant[].class);
        Restaurant[] restaurants = entity.getBody();

        assertEquals(restaurants.length, 15);
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
}
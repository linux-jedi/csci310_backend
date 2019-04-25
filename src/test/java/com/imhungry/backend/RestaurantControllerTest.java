package com.imhungry.backend;

import com.imhungry.backend.data.Restaurant;
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

import static com.imhungry.backend.TestUtilityMethods.register;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = "dev")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestaurantControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    // Show that data can be separated and is enough for a sliding window on the frontend
    @Test
    public void testRestaurantPagination() {
        String uid = register(port, restTemplate);

        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host("localhost")
                .port(port)
                .addPathSegment("restaurant")
                .addQueryParameter("name", "burger")
                .addQueryParameter("amount", "30")
                .addQueryParameter("userid", uid)
                .build();

        ResponseEntity<Restaurant[]> entity = restTemplate.getForEntity(url.toString(), Restaurant[].class);
        Restaurant[] restaurants = entity.getBody();

        assertNotNull(restaurants);
        assertTrue(restaurants.length/5 >= 6);

    }

    @Test
    public void testRadiusDifference() {

        // Test shows that data can be separated and is enough for a sliding window on the frontend
        String uid = register(port, restTemplate);

        // Small Radius
        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host("localhost")
                .port(port)
                .addPathSegment("restaurant")
                .addQueryParameter("name", "burger")
                .addQueryParameter("amount", "5")
                .addQueryParameter("userid", uid)
                .addQueryParameter("radius", "1")
                .build();

        ResponseEntity<Restaurant[]> entity = restTemplate.getForEntity(url.toString(), Restaurant[].class);
        Restaurant[] restaurants = entity.getBody();

        assertNotNull(restaurants);
        assertEquals(1, restaurants.length);


        // Medium Radius
        url = new HttpUrl.Builder()
                .scheme("http")
                .host("localhost")
                .port(port)
                .addPathSegment("restaurant")
                .addQueryParameter("name", "burger")
                .addQueryParameter("amount", "5")
                .addQueryParameter("userid", uid)
                .addQueryParameter("radius", "2")
                .build();

        entity = restTemplate.getForEntity(url.toString(), Restaurant[].class);
        restaurants = entity.getBody();

        assertNotNull(restaurants);
        assertEquals(4, restaurants.length);

        // large radius test
        url = new HttpUrl.Builder()
                .scheme("http")
                .host("localhost")
                .port(port)
                .addPathSegment("restaurant")
                .addQueryParameter("name", "burger")
                .addQueryParameter("amount", "12")
                .addQueryParameter("userid", uid)
                .addQueryParameter("radius", "1.5")
                .build();

        entity = restTemplate.getForEntity(url.toString(), Restaurant[].class);
        restaurants = entity.getBody();

        assertNotNull(restaurants);
        assertEquals(7, restaurants.length);

    }

    @Test
    public void testBadUser() {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host("localhost")
                .port(port)
                .addPathSegment("restaurant")
                .addQueryParameter("userid", "-1")
                .build();

        ResponseEntity<String> entity = restTemplate.getForEntity(url.toString(), String.class);
        assertEquals(404, entity.getStatusCodeValue());

    }

    @Test
    public void testRestaurantSearch() {
        String uid = register(port, restTemplate);
        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host("localhost")
                .port(port)
                .addPathSegment("restaurant")
                .addQueryParameter("name", "burger")
                .addQueryParameter("amount", "5")
                .addQueryParameter("userid", uid)
                .build();

        ResponseEntity<Restaurant[]> entity = restTemplate.getForEntity(url.toString(), Restaurant[].class);
        Restaurant[] restaurants = entity.getBody();

        assertNotNull(restaurants);
        assertEquals(restaurants.length, 5);
        int prev = 0;
        for (Restaurant restaurant: restaurants) {
            assert (restaurant.getTime() >= prev);
            prev = restaurant.getTime();
        }
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

        assertNotNull(r);
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
        assertNotNull(r);
        assertEquals(r.getId(), "ChIJdzrHbse4woARwbth0qmfStw");
    }

}

package com.imhungry.backend;

import com.google.maps.model.PriceLevel;
import okhttp3.HttpUrl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

/**
 * Created by calebthomas on 3/7/19.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ListControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void getListsTest() {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host("localhost")
                .port(port)
                .addPathSegment("list")
                .build();

        ResponseEntity<HungryList[]> responseEntity = restTemplate.getForEntity(url.toString(), HungryList[].class);
        HungryList[] lists = responseEntity.getBody();

        assertEquals(lists.length, 3);
    }

    @Test
    public void getExploreList() {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host("localhost")
                .port(port)
                .addPathSegment("list")
                .addPathSegment("EXPLORE")
                .build();

        ResponseEntity<HungryList> responseEntity = restTemplate.getForEntity(url.toString(), HungryList.class);
        HungryList favoritesList = responseEntity.getBody();

        assertEquals(favoritesList.getName(), HungryList.ListType.EXPLORE.toString());
        assertEquals(favoritesList.getRestaurants().size(), 0);
    }

    @Test
    public void getBlockList() {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host("localhost")
                .port(port)
                .addPathSegment("list")
                .addPathSegment(HungryList.ListType.BLOCK.toString())
                .build();

        ResponseEntity<HungryList> responseEntity = restTemplate.getForEntity(url.toString(), HungryList.class);
        HungryList favoritesList = responseEntity.getBody();

        assertEquals(favoritesList.getName(), HungryList.ListType.BLOCK.toString());
        assertEquals(favoritesList.getRestaurants().size(), 0);
    }

    @Test
    public void getFavoriteList() {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host("localhost")
                .port(port)
                .addPathSegment("list")
                .addPathSegment(HungryList.ListType.FAVORITE.toString())
                .build();

        ResponseEntity<HungryList> responseEntity = restTemplate.getForEntity(url.toString(), HungryList.class);
        HungryList favoritesList = responseEntity.getBody();

        assertEquals(favoritesList.getName(), HungryList.ListType.FAVORITE.toString());
        assertEquals(favoritesList.getRestaurants().size(), 0);
    }

    @Test
    public void getInvalidList() {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host("localhost")
                .port(port)
                .addPathSegment("list")
                .addPathSegment("INVALID")
                .build();

        ResponseEntity<HungryList> responseEntity = restTemplate.getForEntity(url.toString(), HungryList.class);
        HungryList favoritesList = responseEntity.getBody();

        assertEquals(favoritesList.getName(), HungryList.ListType.FAVORITE.toString());
        assertEquals(favoritesList.getRestaurants().size(), 0);
    }

    @Test
    public void testAddRestaurant() throws MalformedURLException {
        // Post restaurant
        Restaurant r = new Restaurant(
                "12345id",
                "Panda Express",
                "12345 McClintock Avenue",
                "1-515-888-8888",
                new URL("http://www.pandaexpress.com"),
                5,
                PriceLevel.MODERATE,
                "9 minutes",
                9 * 60
        );

        HttpUrl postUrl = new HttpUrl.Builder()
                .scheme("http")
                .host("localhost")
                .port(port)
                .addPathSegment("list")
                .addPathSegment(HungryList.ListType.FAVORITE.toString())
                .addPathSegment("restaurant")
                .build();

        ResponseEntity<String> response = restTemplate.postForEntity(postUrl.toString(), r, String.class);

        assertThat(response).isNotNull();
        assertEquals(response.getStatusCodeValue(), 200);
    }

    @Test
    public void testAddRecipe() {
        Recipe r = new Recipe(
                "id",
                "Fried Rice",
                "PHOTO_URL",
                5,
                10,
                new ArrayList<>(),
                "INSTRUCTIONS"
        );

        HttpUrl postUrl = new HttpUrl.Builder()
                .scheme("http")
                .host("localhost")
                .port(port)
                .addPathSegment("list")
                .addPathSegment(HungryList.ListType.FAVORITE.toString())
                .addPathSegment("recipe")
                .build();

        ResponseEntity<String> response = restTemplate.postForEntity(postUrl.toString(), r, String.class);

        assertThat(response).isNotNull();
        assertEquals(response.getStatusCodeValue(), 200);
    }

    @Test
    public void testDeleteRestaurant() throws MalformedURLException {
        // Post restaurant
        Restaurant r = new Restaurant(
                "12345id",
                "Panda Express",
                "12345 McClintock Avenue",
                "1-515-888-8888",
                new URL("http://www.pandaexpress.com"),
                5,
                PriceLevel.MODERATE,
                "9 minutes",
                9 * 360
        );

        HttpUrl postUrl = new HttpUrl.Builder()
                .scheme("http")
                .host("localhost")
                .port(port)
                .addPathSegment("list")
                .addPathSegment(HungryList.ListType.FAVORITE.toString())
                .addPathSegment("restaurant")
                .build();

        ResponseEntity<String> response = restTemplate.postForEntity(postUrl.toString(), r, String.class);

        // Delete just posted restaurant
        HttpUrl deleteURL = new HttpUrl.Builder()
                .scheme("http")
                .host("localhost")
                .port(port)
                .addPathSegment("list")
                .addPathSegment(HungryList.ListType.FAVORITE.toString())
                .addPathSegment("restaurant")
                .addQueryParameter("restaurantId", "12345id")
                .build();

        restTemplate.delete(deleteURL.toString());

        assertThat(response).isNotNull();
        assertEquals(response.getStatusCodeValue(), 200);
    }

    @Test
    public void testDeleteRecipe() {
        // Add recipe to favorite last
        Recipe r = new Recipe(
                "id",
                "Fried Rice",
                "PHOTO_URL",
                5,
                10,
                new ArrayList<>(),
                "INSTRUCTIONS"
        );

        HttpUrl postUrl = new HttpUrl.Builder()
                .scheme("http")
                .host("localhost")
                .port(port)
                .addPathSegment("list")
                .addPathSegment(HungryList.ListType.FAVORITE.toString())
                .addPathSegment("recipe")
                .build();

        ResponseEntity<String> response = restTemplate.postForEntity(postUrl.toString(), r, String.class);

        // Now delete that recipe from list
        HttpUrl deleteURL = new HttpUrl.Builder()
                .scheme("http")
                .host("localhost")
                .port(port)
                .addPathSegment("list")
                .addPathSegment(HungryList.ListType.FAVORITE.toString())
                .addPathSegment("recipe")
                .addQueryParameter("recipeId", "id")
                .build();

        restTemplate.delete(deleteURL.toString());

        assertThat(response).isNotNull();
        assertEquals(response.getStatusCodeValue(), 200);
    }
}

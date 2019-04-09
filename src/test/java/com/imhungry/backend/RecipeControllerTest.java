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

import static org.junit.Assert.assertEquals;

/**
 * Created by calebthomas on 3/7/19.
 */

@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = "prod")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RecipeControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testRecipeSearch() {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host("localhost")
                .port(port)
                .addPathSegment("recipe")
                .addQueryParameter("name", "burger")
                .addQueryParameter("amount", "5")
                .build();

        ResponseEntity<Recipe[]> entity = restTemplate.getForEntity(url.toString(), Recipe[].class);
        Recipe[] recipes = entity.getBody();

        assertEquals(recipes.length, 5);
        int prev = 0;
        for (Recipe recipe : recipes) {
            assert (recipe.getPrepTime() >= prev);
            prev = recipe.getPrepTime();
        }
    }

    @Test
    public void testGetRecipeDetails() {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host("localhost")
                .port(port)
                .addPathSegment("recipe")
                .addPathSegment("573147")
                .build();

        ResponseEntity<Recipe> entity = restTemplate.getForEntity(url.toString(), Recipe.class);
        Recipe r = entity.getBody();

        assertEquals(r.getId(), "573147");
    }
}

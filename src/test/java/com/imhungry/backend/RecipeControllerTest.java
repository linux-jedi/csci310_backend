package com.imhungry.backend;

import com.imhungry.backend.data.Recipe;
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
public class RecipeControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    // Show that data can be separated and is enough for a sliding window on the frontend
    @Test
    public void testRecipePagination() {

        String uid = register(port, restTemplate);

        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host("localhost")
                .port(port)
                .addPathSegment("recipe")
                .addQueryParameter("name", "burger")
                .addQueryParameter("amount", "30")
                .addQueryParameter("userid", uid)
                .build();

        ResponseEntity<Recipe[]> entity = restTemplate.getForEntity(url.toString(), Recipe[].class);
        Recipe[] recipes = entity.getBody();

        assertNotNull(recipes);
        assertTrue(recipes.length/5 >= 6);
    }

    @Test
    public void testRecipeSearch() {
        String uid = register(port, restTemplate);

        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host("localhost")
                .port(port)
                .addPathSegment("recipe")
                .addQueryParameter("name", "burger")
                .addQueryParameter("amount", "5")
                .addQueryParameter("userid", uid)
                .build();

        ResponseEntity<Recipe[]> entity = restTemplate.getForEntity(url.toString(), Recipe[].class);
        Recipe[] recipes = entity.getBody();

        assertNotNull(recipes);
        assertEquals(5, recipes.length);
        int prev = 0;
        for (Recipe recipe : recipes) {
            assert (recipe.getPrepTime() >= prev);
            prev = recipe.getPrepTime();
        }
    }

    @Test
    public void testBadUser() {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host("localhost")
                .port(port)
                .addPathSegment("recipe")
                .addQueryParameter("name", "burger")
                .addQueryParameter("amount", "5")
                .addQueryParameter("userid", "-1")
                .build();

        ResponseEntity<String> entity = restTemplate.getForEntity(url.toString(), String.class);
        assertEquals(404, entity.getStatusCodeValue());
    }

    @Test
    public void testTrueRecipeSearch() {
        String uid = register(port, restTemplate);
        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host("localhost")
                .port(port)
                .addPathSegment("recipe")
                .addQueryParameter("name", "burger")
                .addQueryParameter("amount", "2")
                .addQueryParameter("userid", uid)
                .build();

        ResponseEntity<Recipe[]> entity = restTemplate.getForEntity(url.toString(), Recipe[].class);
        Recipe[] recipes = entity.getBody();

        assertNotNull(recipes);
        assertEquals(2, recipes.length);
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
        Recipe recipe = entity.getBody();

        assertNotNull(recipe);
        assertEquals("573147", recipe.getId());
    }

    @Test
    public void testTrueRecipeDetails() {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host("localhost")
                .port(port)
                .addPathSegment("recipe")
                .addPathSegment("219957")
                .build();

        ResponseEntity<Recipe> entity = restTemplate.getForEntity(url.toString(), Recipe.class);
        Recipe recipe = entity.getBody();

        assertNotNull(recipe);
        assertEquals("219957", recipe.getId());
    }
}

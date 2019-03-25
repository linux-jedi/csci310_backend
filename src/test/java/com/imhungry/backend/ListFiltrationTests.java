package com.imhungry.backend;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created by calebthomas on 3/7/19.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ListFiltrationTests {

	@LocalServerPort
	private int port;
//	private int port = 8080;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void testFavoritesSorting() throws Exception {

		Gson gson = new Gson();
		try {
			String jsonString = readFile("src/test/java/com/imhungry/backend/json/restaurant_result_chinese_5.json");
			List<Restaurant> rests = gson.fromJson(jsonString, new TypeToken<List<Restaurant>>() {}.getType());
			Restaurant third_restaurant = rests.get(2);

			ListManager listManager = new ListManager();
			listManager.getHungryLists().get(0).addRestaurant(third_restaurant);
			List<Restaurant> restaurants = listManager.filterSortRestaurantList(rests);

			assertEquals(third_restaurant.getId(), restaurants.get(0).getId());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testBlockFiltering() throws Exception {

		Gson gson = new Gson();
		try {
			String jsonString = readFile("src/test/java/com/imhungry/backend/json/recipe_result_chinese_5.json");
			List<Recipe> recs = gson.fromJson(jsonString, new TypeToken<List<Recipe>>() {}.getType());
			Recipe third_recipe = recs.get(2);

			ListManager listManager = new ListManager();
			listManager.getHungryLists().get(2).addRecipe(third_recipe);
			List<Recipe> recipes = listManager.filterSortRecipeList(recs);

			assertFalse(recipes.stream().anyMatch(e -> (third_recipe.getId().equals(e.getId()))));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	// Read from File to String
	static String readFile(String path) throws IOException
	{
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded);
	}

}

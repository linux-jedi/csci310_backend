package com.imhungry.backend;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.imhungry.backend.data.Recipe;
import com.imhungry.backend.data.Restaurant;
import com.imhungry.backend.utils.UserListsJsonWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;


@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = "dev")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ListFiltrationTests {

	private Gson gson = new Gson();

	@Test
	public void testFavoritesSorting() throws IOException {

			String jsonString = readFile("src/test/java/com/imhungry/backend/json/restaurant_result_chinese_5.json");
			List<Restaurant> restaurantJson = gson.fromJson(jsonString, new TypeToken<List<Restaurant>>() {}.getType());
			Restaurant thirdRestaurant = restaurantJson.get(2);

			UserListsJsonWrapper userListsJsonWrapper = new UserListsJsonWrapper();
			userListsJsonWrapper.getHungryLists().get(0).addItem(thirdRestaurant);
			List<Restaurant> restaurants = userListsJsonWrapper.filterSortRestaurantList(restaurantJson);

			assertEquals(thirdRestaurant.getId(), restaurants.get(0).getId());

			jsonString = readFile("src/test/java/com/imhungry/backend/json/recipe_result_chinese_5.json");
			List<Recipe> recipeJson = gson.fromJson(jsonString, new TypeToken<List<Recipe>>() {}.getType());
			Recipe thirdRecipe = recipeJson.get(2);

			userListsJsonWrapper = new UserListsJsonWrapper();
			userListsJsonWrapper.getHungryLists().get(0).addItem(thirdRecipe);
			List<Recipe> recipes = userListsJsonWrapper.filterSortRecipeList(recipeJson);

			assertEquals(thirdRecipe.getId(), recipes.get(0).getId());
	}

	@Test
	public void testBlockFiltering() throws IOException {

			String jsonString = readFile("src/test/java/com/imhungry/backend/json/recipe_result_chinese_5.json");
			List<Recipe> recipeJson = gson.fromJson(jsonString, new TypeToken<List<Recipe>>() {}.getType());
			Recipe thirdRecipe = recipeJson.get(2);

			UserListsJsonWrapper userListsJsonWrapper = new UserListsJsonWrapper();
			userListsJsonWrapper.getHungryLists().get(2).addItem(thirdRecipe);
			List<Recipe> recipes = userListsJsonWrapper.filterSortRecipeList(recipeJson);

			assertFalse(recipes.stream().anyMatch(e -> (thirdRecipe.getId().equals(e.getId()))));

			jsonString = readFile("src/test/java/com/imhungry/backend/json/restaurant_result_chinese_5.json");
			List<Restaurant> restaurantJson = gson.fromJson(jsonString, new TypeToken<List<Restaurant>>() {}.getType());
			Restaurant thirdRestaurant = restaurantJson.get(2);

			userListsJsonWrapper = new UserListsJsonWrapper();
			userListsJsonWrapper.getHungryLists().get(2).addItem(thirdRestaurant);
			List<Restaurant> restaurants = userListsJsonWrapper.filterSortRestaurantList(restaurantJson);

			assertFalse(restaurants.stream().anyMatch(e -> (thirdRestaurant.getId().equals(e.getId()))));
	}


	// Read from File to String
	static String readFile(String path) throws IOException
	{
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded);
	}

}

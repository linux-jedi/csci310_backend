package com.imhungry.backend;

import com.imhungry.backend.model.User;
import okhttp3.HttpUrl;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertNotNull;

public class TestUtilityMethods {

	private static int registrationNumber = 0;

	public static void neverRun_onlyForReference() throws Exception {
		RecipeControllerTest recipeControllerTest = new RecipeControllerTest();
		RestaurantControllerTest restaurantControllerTest = new RestaurantControllerTest();
		SearchHistoryTest searchHistoryTest = new SearchHistoryTest();
		GroceryListTest groceryListTest = new GroceryListTest();
		IngredientParsingTest ingredientParsingTest = new IngredientParsingTest();
		ListControllerTest listControllerTest = new ListControllerTest();

		// Pagination

			// Test that enough results can be generated for pagination
			recipeControllerTest.testRecipePagination();

			// Test that enough results can be generated for pagination
			restaurantControllerTest.testRestaurantPagination();

		// Radius
			// TODO: Create radius tests
			restaurantControllerTest.testRadiusDifference();

		// Prior searches

			// Test that most recent result is not included
			searchHistoryTest.checkLastNotIncluded();

			// Test that each search history item has a collage
			searchHistoryTest.collageSearchHistoryTest();

		// Grocery List

			// Test that items can be checked and unchecked
			groceryListTest.checkAndUncheckIngredientTest();

			// Test that items can be collated
			ingredientParsingTest.collateIngredientsTest();

			// Test that items can be parsed correctly
			ingredientParsingTest.parseDifferentIngredients();

			// Test collating of checked items
			ingredientParsingTest.weirdAmountIngredientsCollateTest();

		// Persistence (persistence is checked by doing something to user1, then to user2
		// and then ensuring that user1's data hasn't changed, and then user2's data

			// Check that search history persists
			searchHistoryTest.checkPersistenceTest();

			// Check that grocery list persists
			groceryListTest.checkPersistenceTest();

			// Check that lists persist
			listControllerTest.persistenceTest();

	}

	static String register(int port, TestRestTemplate restTemplate) {
		registrationNumber++;
		String name = "JUNIT_TEST" + registrationNumber;
		HttpUrl url = new HttpUrl.Builder()
				.scheme("http")
				.host("localhost")
				.port(port)
				.addPathSegment("register")
				.addQueryParameter("username", name)
				.addQueryParameter("email", name)
				.addQueryParameter("password", "test")
				.build();

		ResponseEntity<User> responseEntity = restTemplate.postForEntity(url.toString(), new HttpEntity<>(""), User.class);
		User user = responseEntity.getBody();
		assertNotNull(user);
		assertNotNull(user.getId());

		return String.valueOf(user.getId());
	}
}

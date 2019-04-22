package com.imhungry.backend;

public class TestReferenceByFeature {

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
}

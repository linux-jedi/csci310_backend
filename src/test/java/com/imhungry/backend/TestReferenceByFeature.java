package com.imhungry.backend;

public class TestReferenceByFeature {

	public static void neverRun_ForReference() {
		RecipeControllerTest recipeControllerTest = new RecipeControllerTest();
		RestaurantControllerTest restaurantControllerTest = new RestaurantControllerTest();

		// Pagination
		recipeControllerTest.testRecipePagination();
		restaurantControllerTest.testRestaurantPagination();

		// Radius
		restaurantControllerTest.testRadiusDifference();
	}
}

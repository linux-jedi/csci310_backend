package com.imhungry.backend;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BackendApplicationTests {

	@Autowired
	private CollageController collageController;

	@Autowired
	private ListController listController;

	@Autowired
	private RecipeController recipeController;

	@Autowired
	private RestaurantController restaurantController;

	@Test
	public void contextLoads() {
		assertThat(collageController).isNotNull();
		assertThat(listController).isNotNull();
		assertThat(recipeController).isNotNull();
		assertThat(restaurantController).isNotNull();
	}
	
//	@Test
//	public void testConstructRecipe() {
////		Recipe r = new Recipe();
//
//	}
//
//	@Test
//	public void testConstructRestaurant() {
////		Restaurant r = new Restaurant();
//	}
//
//	@Test
//	public void testBuildCollage() {
//		CollageController c = new CollageController();
//
//	}
//	@Test
//	public void testCollageSuccess()  throws IOException {
//		CollageController c = new CollageController();
//		c.getCollage("burgers");
//	}
//
//	public void testCollageTenPhotos() throws IOException {
//		CollageController c = new CollageController();
//		c.getCollage("babaghanoush");
//	}
//
//	public void testCollageNoSearch() throws IOException {
//		CollageController c = new CollageController();
//		c.getCollage("");
//	}
//
//
//	@Test
//	public void testCollageFail() throws IOException {
//		CollageController c = new CollageController();
//		c.getCollage("adfbhklaldafjkljash");
//	}
//
//	@Test
//	public void testSearchSuccess() {
//		RecipeSourcer r1 = new RecipeSourcer();
//		r1.getRecipe("burgers");
//
//		RestaurantSourcer r2  = new RestaurantSourcer();
//		r2.searchRestaurants("burgers", 5);
//
//	}
//
//	@Test
//	public void testSearchMultipleKeywords() {
//		RecipeSourcer r1 = new RecipeSourcer();
//		r1.getRecipe("chinese food");
//
//		RestaurantSourcer r2  = new RestaurantSourcer();
//		r2.searchRestaurants("chinese food", 5);
//	}
//
//	@Test
//	public void testSearchBlank() {
//		RecipeSourcer r1 = new RecipeSourcer();
//		r1.getRecipe("");
//
//		RestaurantSourcer r2  = new RestaurantSourcer();
//		r2.searchRestaurants("", 5);
//	}
//
//	@Test
//	public void testSearchFail() {
//		RecipeSourcer r1 = new RecipeSourcer();
//		r1.getRecipe("adfbhklaldafjkljash");
//
//		RestaurantSourcer r2  = new RestaurantSourcer();
//		r2.searchRestaurants("adfbhklaldafjkljash", 5);
//
//	}

	@Test
	public void testAddToList() {

	}

	@Test
	public void testRemoveFromEmptyList() {

	}


	
	
	
	

}



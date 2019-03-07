package com.imhungry.backend;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BackendApplicationTests {

	@Test
	public void contextLoads() {
	}
	
	@Test
	public void testConstructRecipe() {
		Recipe r = new Recipe(); 
		
	}
	
	@Test
	public void testConstructRestaurant() {
		Restaurant r = new Restaurant(); 
	}
	
	
	
	
	@Test
	public void testBuildCollage() {
		CollageController c = new CollageController(); 
		
	}
	@Test
	public void testCollageSuccess()  {
		CollageController c = new CollageController(); 
		c.getCollage("burgers"); 
	}
	
	public void testCollageTenPhotos() {
		CollageController c = new CollageController(); 
		c.getCollage("babaghanoush"); 
	}
	
	public void testCollageNoSearch() {
		CollageController c = new CollageController(); 
		c.getCollage(""); 
	}
	
	
	@Test
	public void testCollageFail()  {
		CollageController c = new CollageController(); 
		c.getCollage("adfbhklaldafjkljash"); 
	}
	
	@Test
	public void testSearchSuccess() {
		RecipeSourcer r1 = new RecipeSourcer(); 
		r1.getRecipe("burgers"); 
		
		RestaurantSourcer r2  = new RestaurantSourcer(); 
		r2.searchRestaurants("burgers", 5); 
		
	}
	
	@Test
	public void testSearchMultipleKeywords() {
		RecipeSourcer r1 = new RecipeSourcer(); 
		r1.getRecipe("chinese food"); 
		
		RestaurantSourcer r2  = new RestaurantSourcer(); 
		r2.searchRestaurants("chinese food", 5); 
	}
	
	@Test
	public void testSearchBlank() {
		RecipeSourcer r1 = new RecipeSourcer(); 
		r1.getRecipe(""); 
		
		RestaurantSourcer r2  = new RestaurantSourcer(); 
		r2.searchRestaurants("", 5);
	}
	
	@Test
	public void testSearchFail() {
		RecipeSourcer r1 = new RecipeSourcer(); 
		r1.getRecipe("adfbhklaldafjkljash"); 
		
		RestaurantSourcer r2  = new RestaurantSourcer(); 
		r2.searchRestaurants("adfbhklaldafjkljash", 5);		
		
	}
	
	@Test 
	public void testAddToList() {
		
	}
	
	@Test
	public void testRemoveFromEmptyList() {
		
	}
	

	
	
	
	

}



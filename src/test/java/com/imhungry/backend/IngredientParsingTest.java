package com.imhungry.backend;

import com.imhungry.backend.controller.GroceryListController;
import com.imhungry.backend.model.Ingredient;
import com.imhungry.backend.sourcer.IngredientParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = "dev")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IngredientParsingTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private GroceryListController groceryListController;

	// Ensure different formats of ingredients can be parsed
	@Test
	public void parseDifferentIngredients() {
		IngredientParser ingredientParser = new IngredientParser("1 cup flour");
		assertEquals(Double.valueOf(1), ingredientParser.getQuantity());
		assertEquals("cup flour", ingredientParser.getIngredientValue());

		ingredientParser = new IngredientParser("½ cup flour");
		assertEquals(Double.valueOf(0.5), ingredientParser.getQuantity());
		assertEquals("cup flour", ingredientParser.getIngredientValue());

		ingredientParser = new IngredientParser("2½ cup flour");
		assertEquals(ingredientParser.getQuantity(), new Double(2.5));
		assertEquals("cup flour", ingredientParser.getIngredientValue());

		ingredientParser = new IngredientParser("¼ cup flour");
		assertEquals(Double.valueOf(0.25), ingredientParser.getQuantity());
		assertEquals("cup flour", ingredientParser.getIngredientValue());

		ingredientParser = new IngredientParser("1.5 cup flour");
		assertEquals(Double.valueOf(1.5), ingredientParser.getQuantity());
		assertEquals("cup flour", ingredientParser.getIngredientValue());

		ingredientParser = new IngredientParser("cup flour");
		assertNull(ingredientParser.getQuantity());
		assertEquals("cup flour", ingredientParser.getIngredientValue());

	}

	// Test that items can be merged/collated correctly
	@Test
	public void collateIngredientsTest() {
		String uid = register();
		GroceryListTest groceryListTest = new GroceryListTest();

		groceryListController.addIngredient("1 cup flour", Long.parseLong(uid));
		groceryListController.addIngredient("1.2 cup flour", Long.parseLong(uid));
		groceryListController.addIngredient("1.5 cup flour", Long.parseLong(uid));
		groceryListController.addIngredient("1 cup butter", Long.parseLong(uid));

		Ingredient[] ingredients = groceryListTest.getGroceryList(uid, port, restTemplate);
		assertEquals("cup flour", ingredients[0].getIngredientValue());
		assertEquals(Double.valueOf(3.7), ingredients[0].getQuantity());
		assertEquals("cup butter", ingredients[1].getIngredientValue());
		assertEquals(Double.valueOf(1), ingredients[1].getQuantity());
	}

	@Test
	public void secondCollateIngredientsTest() {
		String uid = register();
		GroceryListTest groceryListTest = new GroceryListTest();

		groceryListController.addIngredient("1 tbsp soft brown sugar", Long.parseLong(uid));
		groceryListController.addIngredient("1 tbsp soft brown sugar", Long.parseLong(uid));
		groceryListController.addIngredient("1 tbsp soft brown sugar", Long.parseLong(uid));
		groceryListController.addIngredient("1 tbsp soft brown sugar", Long.parseLong(uid));

		Ingredient[] ingredients = groceryListTest.getGroceryList(uid, port, restTemplate);
		assertEquals("tbsp soft brown sugar", ingredients[0].getIngredientValue());
		assertEquals(Double.valueOf(4), ingredients[0].getQuantity());

	}

	// Ensure that items with different checked "states" are merged correctly
	@Test
	public void collateCheckedUncheckedItemsTest() {
		assertCheckedCorrectMerge(true, true, true);
		assertCheckedCorrectMerge(true, false, true);
		assertCheckedCorrectMerge(false, true, true);
		assertCheckedCorrectMerge(false, false, false);
	}

	// Merge two items with given check values, and expect them to have a certain checked value when merged
	private void assertCheckedCorrectMerge(boolean aCheck, boolean bCheck, boolean expected) {
		Ingredient a = new Ingredient();
		buildIngredient(a);

		a.setChecked(aCheck);

		Ingredient b = new Ingredient();
		buildIngredient(b);

		b.setChecked(bCheck);

		IngredientParser.collateIngredients(a, b);
		assertEquals(expected, a.isChecked());
	}

	void buildIngredient(Ingredient a) {
		a.setChecked(true);
		a.setIngredientString("ingredient");
		a.setQuantity(1d);
		a.setUserId(1L);
		a.setIngredientValue("ingredient");
		a.setId(1L);
	}

	private String register() {
		return TestUtilityMethods.register(port, restTemplate);
	}

}

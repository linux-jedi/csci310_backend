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

import static com.imhungry.backend.GroceryListTest.register;
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

	private String registerNewUser(String name) {
		return register(name, port, restTemplate);
	}
	@Test
	public void parseDifferentIngredients() {
		IngredientParser ingredientParser = new IngredientParser("1 cup flour");
		assertEquals(ingredientParser.getQuantity(), new Double(1));
		assertEquals("cup flour", ingredientParser.getIngredientValue());

		ingredientParser = new IngredientParser("½ cup flour");
		assertEquals(ingredientParser.getQuantity(), new Double(0.5));
		assertEquals("cup flour", ingredientParser.getIngredientValue());

		ingredientParser = new IngredientParser("¼ cup flour");
		assertEquals(ingredientParser.getQuantity(), new Double(0.25));
		assertEquals("cup flour", ingredientParser.getIngredientValue());

		ingredientParser = new IngredientParser("1.5 cup flour");
		assertEquals(ingredientParser.getQuantity(), new Double(1.5));
		assertEquals("cup flour", ingredientParser.getIngredientValue());

		ingredientParser = new IngredientParser("cup flour");
		assertNull(ingredientParser.getQuantity());
		assertEquals("cup flour", ingredientParser.getIngredientValue());

	}

	@Test
	public void collateIngredientsTest() {
		String uid = registerNewUser("collateIngredientsTest");
		GroceryListTest groceryListTest = new GroceryListTest();

		groceryListController.addIngredient("1 cup flour", Long.parseLong(uid));
		groceryListController.addIngredient("1.2 cup flour", Long.parseLong(uid));
		groceryListController.addIngredient("1 cup butter", Long.parseLong(uid));

		Ingredient[] ingredients = groceryListTest.getGroceryList(uid, port, restTemplate);
		assertEquals("cup flour", ingredients[0].getIngredientValue());
		assertEquals(new Double(2.2), ingredients[0].getQuantity());
		assertEquals("cup butter", ingredients[1].getIngredientValue());
		assertEquals(new Double(1), ingredients[1].getQuantity());
	}
}
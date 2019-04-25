package com.imhungry.backend;

import com.imhungry.backend.model.Ingredient;
import okhttp3.HttpUrl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = "dev")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GroceryListTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	private String register() {
		return TestUtilityMethods.register(port, restTemplate);
	}

	@Test
	public void collateIngredientTest() {
		String uid1 = register();
		final String ADD = "0.5 cup ingredient";
		List<String> adding = new ArrayList<>();
		adding.add(ADD);
		adding.add(ADD);
		adding.add(ADD);

		addNewIngredients(adding, uid1);

		Ingredient[] ingredients = getGroceryList(uid1);
		assertNotNull(ingredients);
		assertEquals(1, ingredients.length);
		assertEquals("1.5 cup ingredient", ingredients[0].getIngredientString());
	}

	@Test
	public void checkPersistence() {
		String uid1 = register();
		String uid2 = register();

		final String ADD = "0.5 cup ingredient";
		List<String> adding = new ArrayList<>();
		adding.add(ADD + "1");
		adding.add(ADD + "2");

		// Add items to user 1's ingredients
		addNewIngredients(adding, uid1);
		adding.add(ADD + "3");

		// Add items to user 2's ingredients
		addNewIngredients(adding, uid2);

		// Show correctness of user 1's ingredients
		Ingredient[] ingredients = getGroceryList(uid1);
		assertNotNull(ingredients);
		assertEquals(2, ingredients.length);

		// Show correctness of user 2's ingredients
		ingredients = getGroceryList(uid2);
		assertNotNull(ingredients);
		assertEquals(3, ingredients.length);
	}

	@Test
	public void getIngredientTest() {
		String uid = register();
		final String ADD = "0.5 cup ingredient";
		addNewIngredient(ADD, uid);

		Ingredient[] ingredients = getGroceryList(uid);

		assertNotNull(ingredients);
		assertEquals(1, ingredients.length);
		assertEquals("cup ingredient", ingredients[0].getIngredientValue());
		assertEquals(Double.valueOf(0.5), ingredients[0].getQuantity());
		assertEquals(ADD, ingredients[0].getIngredientString());
	}

	@Test
	public void addIngredientsTest() {
		String uid = register();
		final String ADD = "0.5 cup ingredient";
		List<String> adding = new ArrayList<>();
		adding.add(ADD);
		adding.add(ADD);

		addNewIngredients(adding, uid);

		Ingredient[] ingredients = getGroceryList(uid);
		assertNotNull(ingredients);
		assertEquals(1, ingredients.length);
	}

	@Test
	public void addNewSameIngredient() {
		String uid = register();
		final String ADD = "0.5 cup ingredient";
		for (int i = 0; i < 5; i++) {
			addNewIngredient(ADD, uid);
		}

		Ingredient[] ingredients = getGroceryList(uid);
		assertNotNull(ingredients);
		assertEquals(1, ingredients.length);
		assertEquals(Double.valueOf(2.5), ingredients[0].getQuantity());
	}


	// Show ingredients can be checked and unchecked
	@Test
	public void checkAndUncheckIngredientTest() {
		String uid = register();

		// Add an item
		final String ADD = "0.5 cup ingredient";
		List<String> adding = new ArrayList<>();
		adding.add(ADD);
		addNewIngredients(adding, uid);

		// Get the grocery list
		Ingredient[] ingredients = getGroceryList(uid);
		Ingredient i = ingredients[0];
		String id = String.valueOf(i.getId());

		// Should be unchecked by default
		assertFalse(i.isChecked());

		// Mark it as checked
		checkIngredient(id, uid);

		// Get the grocery list and ensure item is now checked
		ingredients = getGroceryList(uid);
		i = ingredients[0];
		id = String.valueOf(i.getId());
		assertTrue(i.isChecked());

		// Mark it as unchecked
		uncheckIngredient(id, uid);

		// Get the grocery list and ensure item is now unchecked
		ingredients = getGroceryList(uid);
		i = ingredients[0];
		assertFalse(i.isChecked());
	}

	@Test
	public void deleteIngredientTest() {
		String uid = register();
		final String ADD = "0.5 cup ingredient";
		for (int i = 0; i < 5; i++) {
			addNewIngredient(ADD + String.valueOf(i), uid);
		}

		Ingredient[] ingredients = getGroceryList(uid);

		HttpUrl url = new HttpUrl.Builder()
				.scheme("http")
				.host("localhost")
				.port(port)
				.addPathSegment("grocery")
				.addPathSegment("deleteItem")
				.addQueryParameter("userid", uid)
				.addQueryParameter("ingredientid", String.valueOf(ingredients[0].getId()))
				.build();

		restTemplate.delete(url.toString());

		ingredients = getGroceryList(uid);
		assertNotNull(ingredients);
		assertEquals(4, ingredients.length);
	}

	@Test
	public void badCheckIngredientTest() {
		String uid = register();
		final String ADD = "0.5 cup ingredient";
		List<String> adding = new ArrayList<>();
		adding.add(ADD);
		addNewIngredients(adding, uid);

		Ingredient[] ingredients = getGroceryList(uid);
		Ingredient i = ingredients[0];

		assertFalse(i.isChecked());

		checkIngredient("-1", uid);
		uncheckIngredient("-1", uid);
	}

	@Test
	public void badDeleteIngredientTest() {
		String uid = register();
		final String ADD = "0.5 cup ingredient";
		for (int i = 0; i < 5; i++) {
			addNewIngredient(ADD + String.valueOf(i), uid);
		}

		HttpUrl url = new HttpUrl.Builder()
				.scheme("http")
				.host("localhost")
				.port(port)
				.addPathSegment("grocery")
				.addPathSegment("deleteItem")
				.addQueryParameter("userid", uid)
				.addQueryParameter("ingredientid", "-1")
				.build();

		restTemplate.delete(url.toString());

		url = new HttpUrl.Builder()
				.scheme("http")
				.host("localhost")
				.port(port)
				.addPathSegment("grocery")
				.addPathSegment("deleteItem")
				.addQueryParameter("userid", "-1")
				.addQueryParameter("ingredientid", "-1")
				.build();

		restTemplate.delete(url.toString());
	}

	private void addNewIngredients(List<String> adding, String uid) {
		HttpUrl url = new HttpUrl.Builder()
				.scheme("http")
				.host("localhost")
				.port(port)
				.addPathSegment("grocery")
				.addPathSegment("addItems")
				.addQueryParameter("userid", uid)
				.build();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<List<String>> entity = new HttpEntity<>(adding, headers);
		restTemplate.postForEntity(url.toString(), entity, String.class);
	}

	private void addNewIngredient(String ingredientName, String uid) {
		HttpUrl url = new HttpUrl.Builder()
				.scheme("http")
				.host("localhost")
				.port(port)
				.addPathSegment("grocery")
				.addPathSegment("addItem")
				.addQueryParameter("userid", uid)
				.build();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.TEXT_PLAIN);

		HttpEntity<String> entity = new HttpEntity<>(ingredientName, headers);
		restTemplate.postForEntity(url.toString(), entity, String.class);
	}

	private Ingredient[] getGroceryList(String uid) {
		return getGroceryList(uid, port, restTemplate);
	}

	Ingredient[] getGroceryList(String uid, int myPort, TestRestTemplate restTemplate) {
		HttpUrl url;
		url = new HttpUrl.Builder()
				.scheme("http")
				.host("localhost")
				.port(myPort)
				.addPathSegment("grocery")
				.addQueryParameter("userid", uid)
				.build();

		ResponseEntity<Ingredient[]> res = restTemplate.getForEntity(url.toString(), Ingredient[].class);
		return res.getBody();
	}

	private void checkIngredient(String id, String uid) {
		HttpUrl url;
		url = new HttpUrl.Builder()
				.scheme("http")
				.host("localhost")
				.port(port)
				.addPathSegment("grocery")
				.addPathSegment("check")
				.addQueryParameter("userid", uid)
				.addQueryParameter("ingredientid", id)
				.build();

		restTemplate.put(url.toString(), null);
	}

	private void uncheckIngredient(String id, String uid) {
		HttpUrl url;
		url = new HttpUrl.Builder()
				.scheme("http")
				.host("localhost")
				.port(port)
				.addPathSegment("grocery")
				.addPathSegment("uncheck")
				.addQueryParameter("userid", uid)
				.addQueryParameter("ingredientid", id)
				.build();

		restTemplate.put(url.toString(), null);
	}


}

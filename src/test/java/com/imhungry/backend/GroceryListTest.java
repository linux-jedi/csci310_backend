package com.imhungry.backend;

import com.imhungry.backend.model.Ingredient;
import com.imhungry.backend.model.User;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = "dev")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GroceryListTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	private String registerNewUser(String name) {
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

	@Test
	public void getIngredientTest() {
		String uid = registerNewUser("getIngredientTest");
		final String ADD = "1/2 cup ingredient";
		addNewIngredient(ADD, uid);

		Ingredient[] ingredients = getGroceryList(uid);

		assertNotNull(ingredients);
		assertEquals(ingredients.length, 1);
		assertEquals(ingredients[0].getIngredientValue(), ADD);
	}

	@Test
	public void addIngredientsTest() {
		String uid = registerNewUser("addIngredientTest");
		final String ADD = "1/2 cup ingredient";
		List<String> adding = new ArrayList<>();
		adding.add(ADD);
		adding.add(ADD);

		addNewIngredients(adding, uid);

		Ingredient[] ingredients = getGroceryList(uid);
		assertNotNull(ingredients);
		assertEquals(ingredients.length, 2);
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

	@Test
	public void deleteIngredientTest() {
		String uid = registerNewUser("deleteIngredientTest");
		final String ADD = "1/2 cup ingredient";
		for (int i = 0; i < 5; i++) {
			addNewIngredient(ADD, uid);
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
		assertEquals(ingredients.length, 4);
	}

	@Test
	public void deleteBadIngredientTest() {
		String uid = registerNewUser("deleteBadIngredientTest");
		final String ADD = "1/2 cup ingredient";
		for (int i = 0; i < 5; i++) {
			addNewIngredient(ADD, uid);
		}

		Ingredient[] ingredients = getGroceryList(uid);

		HttpUrl url = new HttpUrl.Builder()
				.scheme("http")
				.host("localhost")
				.port(port)
				.addPathSegment("grocery")
				.addPathSegment("deleteItem")
				.addQueryParameter("userid", uid)
				.addQueryParameter("ingredientid", "100000")
				.build();

		restTemplate.delete(url.toString());

		ingredients = getGroceryList(uid);
		assertNotNull(ingredients);
		assertEquals(ingredients.length, 5);
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
		HttpUrl url;
		url = new HttpUrl.Builder()
				.scheme("http")
				.host("localhost")
				.port(port)
				.addPathSegment("grocery")
				.addQueryParameter("userid", uid)
				.build();

		ResponseEntity<Ingredient[]> res = restTemplate.getForEntity(url.toString(), Ingredient[].class);
		return res.getBody();
	}



}

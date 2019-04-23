package com.imhungry.backend;

import com.google.maps.model.PriceLevel;
import com.imhungry.backend.data.HungryList;
import com.imhungry.backend.data.Recipe;
import com.imhungry.backend.data.Restaurant;
import okhttp3.HttpUrl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = "dev")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ListControllerTest {

	//TODO: Rename all tests to start with 'test'

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	private static int restaurantRecipeCounter = 0;

	private String register() {
		return TestUtilityMethods.register(port, restTemplate);
	}

	@Test
	public void persistenceTest() throws MalformedURLException {
		String uid1 = register();
		String uid2 = register();

		addRestaurantToFavorites(uid1);
		addRestaurantToFavorites(uid1);
		addRestaurantToFavorites(uid1);

		addRestaurantToFavorites(uid2);
		addRestaurantToFavorites(uid2);

		HungryList hungryList1 = getList(uid1, HungryList.ListType.FAVORITE.toString());
		HungryList hungryList2 = getList(uid2, HungryList.ListType.FAVORITE.toString());

		assertEquals(3, hungryList1.getRestaurants().size());
		assertEquals(2, hungryList2.getRestaurants().size());
	}

	@Test
	public void invalidUserTest() {
		HttpUrl url = new HttpUrl.Builder()
				.scheme("http")
				.host("localhost")
				.port(port)
				.addPathSegment("list")
				.addQueryParameter("userId", "-1")
				.build();

		ResponseEntity<String> responseEntity = restTemplate.getForEntity(url.toString(), String.class);
		HttpStatus statusCode  = responseEntity.getStatusCode();

		assertEquals(404, statusCode.value());
	}

	@Test
	public void getAllLists() {
		String uid1 = register();
		ArrayList<String> vals = Arrays
				.stream(HungryList.ListType.values())
				.map(Enum::toString)
				.collect(Collectors.toCollection(ArrayList::new));

		for (String v : vals) {
			HungryList list = getList(uid1, v);
			assertNotNull(list);
			assertEquals(list.getName(), v.toString());
			assertEquals(list.getRestaurants().size(), 0);
		}
	}

	@Test
	public void testUpdateList() throws MalformedURLException {
		String uid1 = register();
		for (HungryList.ListType v: HungryList.ListType.values()) {
			checkUpdateList(v.toString(), uid1);
		}
	}

	@Test
	public void testBadPut() throws MalformedURLException {
		String uid1 = register();
		Restaurant r = generateNewRestaurant();

		HungryList list = new HungryList(HungryList.ListType.FAVORITE.toString());
			list.addItem(r);

			// Update list request
			HttpUrl putUrl = new HttpUrl.Builder()
					.scheme("http")
					.host("localhost")
					.port(port)
					.addPathSegment("list")
					.addEncodedPathSegment("RANDOM")
					.addQueryParameter("userId", uid1)
					.build();

			HttpEntity<HungryList> putUpdate = new HttpEntity<>(list);
			restTemplate.exchange(putUrl.toString(), HttpMethod.PUT, putUpdate, Void.class);

			// Check that list was updated
			HttpUrl url = new HttpUrl.Builder()
					.scheme("http")
					.host("localhost")
					.port(port)
					.addPathSegment("list")
					.addPathSegment("RANDOM")
					.addQueryParameter("userId", uid1)
					.build();

			ResponseEntity<HungryList> responseEntity = restTemplate.getForEntity(url.toString(), HungryList.class);
			HungryList shouldNotExist = responseEntity.getBody();

			assertNull(shouldNotExist);

	}

	@Test
	public void testAddRestaurant() throws MalformedURLException {
		String uid1 = register();
		Restaurant r = generateNewRestaurant();

		ResponseEntity<String> response = addRestaurantToFavorites(uid1);

		assertThat(response).isNotNull();
		assertEquals(response.getStatusCodeValue(), 200);
	}

	@Test
	public void testAddDuplicate() throws MalformedURLException {
		String uid1 = register();
		Restaurant r = generateNewRestaurant();

		addRestaurantToFavorites(uid1, 1);
		addRestaurantToFavorites(uid1, 1);


		HungryList favoritesList = getList(uid1, HungryList.ListType.FAVORITE.toString());

		assertNotNull(favoritesList);
		assertEquals(favoritesList.getItems().size(), 1);
	}

	@Test
	public void testAddRecipe() {
		String uid1 = register();
		ResponseEntity<String> response = addRecipeToFavorites(uid1);

		assertThat(response).isNotNull();
		assertEquals(response.getStatusCodeValue(), 200);
	}

	@Test
	public void testDeleteRestaurant() throws MalformedURLException {
		// Post restaurant
		Restaurant r = generateNewRestaurant();
		String uid1 = register();


		ResponseEntity<String> response = addRestaurantToFavorites(uid1);

		// Delete just posted restaurant
		HttpUrl deleteURL = new HttpUrl.Builder()
				.scheme("http")
				.host("localhost")
				.port(port)
				.addPathSegment("list")
				.addPathSegment(HungryList.ListType.FAVORITE.toString())
				.addPathSegment("restaurant")
				.addQueryParameter("restaurantId", "12345id")
				.addQueryParameter("userId", uid1)
				.build();

		restTemplate.delete(deleteURL.toString());

		assertThat(response).isNotNull();
		assertEquals(response.getStatusCodeValue(), 200);
	}

	@Test
	public void testDeleteRecipe() {
		String uid1 = register();
		ResponseEntity<String> response = addRecipeToFavorites(uid1);

		// Now delete that recipe from list
		HttpUrl deleteURL = new HttpUrl.Builder()
				.scheme("http")
				.host("localhost")
				.port(port)
				.addPathSegment("list")
				.addPathSegment(HungryList.ListType.FAVORITE.toString())
				.addPathSegment("recipe")
				.addQueryParameter("recipeId", "id")
				.addQueryParameter("userId", uid1)
				.build();

		restTemplate.delete(deleteURL.toString());

		assertThat(response).isNotNull();
		assertEquals(response.getStatusCodeValue(), 200);
	}

	private ResponseEntity<String> addRecipeToFavorites(String uid1) {
		Recipe item = generateNewRecipe();
		HttpUrl postUrl = new HttpUrl.Builder()
				.scheme("http")
				.host("localhost")
				.port(port)
				.addPathSegment("list")
				.addPathSegment(HungryList.ListType.FAVORITE.toString())
				.addPathSegment("recipe")
				.addQueryParameter("userId", uid1)
				.build();

		return restTemplate.postForEntity(postUrl.toString(), item, String.class);
	}

	private ResponseEntity<String> addRestaurantToFavorites(String uid, int i) throws MalformedURLException {
		HttpUrl postUrl = new HttpUrl.Builder()
				.scheme("http")
				.host("localhost")
				.port(port)
				.addPathSegment("list")
				.addPathSegment(HungryList.ListType.FAVORITE.toString())
				.addPathSegment("restaurant")
				.addQueryParameter("userId", uid)
				.build();

		if (i == 0)
			return restTemplate.postForEntity(postUrl.toString(), generateNewRestaurant(), String.class);
		return restTemplate.postForEntity(postUrl.toString(), generateNewRestaurant(i), String.class);
	}

	private ResponseEntity<String> addRestaurantToFavorites(String uid) throws MalformedURLException {
		return addRestaurantToFavorites(uid, 0);
	}

	private void checkUpdateList(String listName, String uid1) throws MalformedURLException {
		// Setup test list
		Restaurant r = generateNewRestaurant();

		HungryList list = new HungryList(listName);
		list.addItem(r);

		// Update list request
		HttpUrl putUrl = new HttpUrl.Builder()
				.scheme("http")
				.host("localhost")
				.port(port)
				.addPathSegment("list")
				.addPathSegment(list.getName())
				.addQueryParameter("userId", uid1)
				.build();

		HttpEntity<HungryList> putUpdate = new HttpEntity<>(list);
		restTemplate.exchange(putUrl.toString(), HttpMethod.PUT, putUpdate, Void.class);

		// Check that list was updated
		HttpUrl url = new HttpUrl.Builder()
				.scheme("http")
				.host("localhost")
				.port(port)
				.addPathSegment("list")
				.addPathSegment(listName)
				.addQueryParameter("userId", uid1)
				.build();

		ResponseEntity<HungryList> responseEntity = restTemplate.getForEntity(url.toString(), HungryList.class);
		HungryList favoritesList = responseEntity.getBody();

		assertNotNull(favoritesList);
		assertEquals(favoritesList.getRestaurants().size(), 1);
	}

	private HungryList getList(String uid, String list) {
		HttpUrl url = new HttpUrl.Builder()
				.scheme("http")
				.host("localhost")
				.port(port)
				.addPathSegment("list")
				.addPathSegment(list)
				.addQueryParameter("userId", uid)
				.build();

		ResponseEntity<HungryList> responseEntity = restTemplate.getForEntity(url.toString(), HungryList.class);
		return responseEntity.getBody();
	}

	private Restaurant generateNewRestaurant() throws MalformedURLException {
		return generateNewRestaurant(restaurantRecipeCounter++);
	}

	private Restaurant generateNewRestaurant(int i) throws MalformedURLException {
		return new Restaurant(
				"12345id",
				"Panda Express",
				"12345 McClintock Avenue",
				"1-515-888-8888",
				new URL("http://www.pandaexpress.com"),
				5,
				PriceLevel.MODERATE,
				"9 minutes",
				9 * i
		);
	}

	private Recipe generateNewRecipe() {
		restaurantRecipeCounter++;
		return new Recipe(
				String.valueOf(restaurantRecipeCounter),
				"Fried Rice",
				"PHOTO_URL",
				5,
				10,
				new ArrayList<>(),
				"INSTRUCTIONS"
		);
	}

}

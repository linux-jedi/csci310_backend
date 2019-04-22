package com.imhungry.backend;

import com.google.maps.model.PriceLevel;
import com.imhungry.backend.data.HungryList;
import com.imhungry.backend.data.Recipe;
import com.imhungry.backend.data.Restaurant;
import com.imhungry.backend.model.User;
import com.imhungry.backend.model.UserLists;
import com.imhungry.backend.repository.UserListsRepository;
import com.imhungry.backend.repository.UserRepository;
import com.imhungry.backend.utils.UserListsJsonWrapper;
import okhttp3.HttpUrl;
import org.junit.Before;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static com.imhungry.backend.GroceryListTest.register;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;


@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = "dev")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ListControllerTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserListsRepository userListsRepository;

	private long cID = 0;

	private String registerNewUser(String name) {
		return register(name, port, restTemplate);
	}

	@Before
	public void createUser() {
		User u = new User();
		u.setUsername("caleb");
		u.setEmail("caleb@usc.edu");
		// you have to do some special password encoding
		u.setPassword(passwordEncoder.encode("test"));

		cID = userRepository.save(u).getId();

		UserLists lists = new UserLists();
		lists.setUserId(u.getId());
		lists.setUserListsJsonWrapper(new UserListsJsonWrapper());
		userListsRepository.save(lists);

	}

	@Test
	public void persistenceTest() throws MalformedURLException {
		String uid1 = registerNewUser("ListController.checkPersistenceTest1");
		String uid2 = registerNewUser("ListController.checkPersistenceTest2");

		addRestaurantToList(uid1, generateRestaurant(1), HungryList.ListType.FAVORITE.toString());
		addRestaurantToList(uid1, generateRestaurant(2), HungryList.ListType.FAVORITE.toString());
		addRestaurantToList(uid1, generateRestaurant(3), HungryList.ListType.FAVORITE.toString());

		addRestaurantToList(uid2, generateRestaurant(4), HungryList.ListType.FAVORITE.toString());
		addRestaurantToList(uid2, generateRestaurant(5), HungryList.ListType.FAVORITE.toString());

		HungryList hungryList1 = getList(uid1, HungryList.ListType.FAVORITE);
		HungryList hungryList2 = getList(uid2, HungryList.ListType.FAVORITE);

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
	public void getListsTest() {
		HttpUrl url = new HttpUrl.Builder()
				.scheme("http")
				.host("localhost")
				.port(port)
				.addPathSegment("list")
				.addQueryParameter("userId", String.valueOf(cID))
				.build();

		ResponseEntity<HungryList[]> responseEntity = restTemplate.getForEntity(url.toString(), HungryList[].class);
		HungryList[] lists = responseEntity.getBody();

		assertNotNull(lists);
		assertEquals(lists.length, 3);
	}

	@Test
	public void getExploreList() {
		HttpUrl url = new HttpUrl.Builder()
				.scheme("http")
				.host("localhost")
				.port(port)
				.addPathSegment("list")
				.addPathSegment("EXPLORE")
				.addQueryParameter("userId", String.valueOf(cID))
				.build();

		ResponseEntity<HungryList> responseEntity = restTemplate.getForEntity(url.toString(), HungryList.class);
		HungryList favoritesList = responseEntity.getBody();

		assertNotNull(favoritesList);
		assertEquals(favoritesList.getName(), HungryList.ListType.EXPLORE.toString());
		assertEquals(favoritesList.getRestaurants().size(), 0);
	}

	@Test
	public void getBlockList() {
		HungryList favoritesList = getList(String.valueOf(cID), HungryList.ListType.BLOCK);

		assertNotNull(favoritesList);
		assertEquals(favoritesList.getName(), HungryList.ListType.BLOCK.toString());
		assertEquals(favoritesList.getRestaurants().size(), 0);
	}

	@Test
	public void getFavoriteListTest() {
		HungryList favoritesList = getList(String.valueOf(cID), HungryList.ListType.FAVORITE);

		assertNotNull(favoritesList);
		assertEquals(favoritesList.getName(), HungryList.ListType.FAVORITE.toString());
		assertEquals(favoritesList.getRestaurants().size(), 0);
	}

	HungryList getList(String uid, HungryList.ListType favorite) {
		HttpUrl url = new HttpUrl.Builder()
				.scheme("http")
				.host("localhost")
				.port(port)
				.addPathSegment("list")
				.addPathSegment(favorite.toString())
				.addQueryParameter("userId", uid)
				.build();

		ResponseEntity<HungryList> responseEntity = restTemplate.getForEntity(url.toString(), HungryList.class);
		return responseEntity.getBody();
	}

	@Test
	public void testUpdateList() throws MalformedURLException {
		checkUpdateList(HungryList.ListType.FAVORITE.toString());
		checkUpdateList(HungryList.ListType.BLOCK.toString());
		checkUpdateList(HungryList.ListType.EXPLORE.toString());
	}

	@Test
	public void testBadPut() throws MalformedURLException {
			// Setup test list
		Restaurant r = generateRestaurant(60);

		HungryList list = new HungryList(HungryList.ListType.FAVORITE.toString());
			list.addItem(r);

			// Update list request
			HttpUrl putUrl = new HttpUrl.Builder()
					.scheme("http")
					.host("localhost")
					.port(port)
					.addPathSegment("list")
					.addEncodedPathSegment("RANDOM")
					.addQueryParameter("userId", String.valueOf(cID))
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
					.addQueryParameter("userId", String.valueOf(cID))
					.build();

			ResponseEntity<HungryList> responseEntity = restTemplate.getForEntity(url.toString(), HungryList.class);
			HungryList favoritesList = responseEntity.getBody();

			assertNull(favoritesList);

	}

	private void checkUpdateList(String listName) throws MalformedURLException {
		// Setup test list
		Restaurant r = generateRestaurant(60);

		HungryList list = new HungryList(listName);
		list.addItem(r);

		// Update list request
		HttpUrl putUrl = new HttpUrl.Builder()
				.scheme("http")
				.host("localhost")
				.port(port)
				.addPathSegment("list")
				.addEncodedPathSegment(list.getName())
				.addQueryParameter("userId", String.valueOf(cID))
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
				.addQueryParameter("userId", String.valueOf(cID))
				.build();

		ResponseEntity<HungryList> responseEntity = restTemplate.getForEntity(url.toString(), HungryList.class);
		HungryList favoritesList = responseEntity.getBody();

		assertNotNull(favoritesList);
		assertEquals(favoritesList.getRestaurants().size(), 1);
	}



	@Test
	public void testAddRestaurant() throws MalformedURLException {
		// Post restaurant
		Restaurant r = generateRestaurant(60);

		ResponseEntity<String> response = addRestaurantToList(String.valueOf(cID), r, HungryList.ListType.FAVORITE.toString());

		assertThat(response).isNotNull();
		assertEquals(response.getStatusCodeValue(), 200);
	}

	ResponseEntity<String> addRestaurantToList(String uid, Restaurant r, String list) {
		HttpUrl postUrl = new HttpUrl.Builder()
				.scheme("http")
				.host("localhost")
				.port(port)
				.addPathSegment("list")
				.addPathSegment(list)
				.addPathSegment("restaurant")
				.addQueryParameter("userId", uid)
				.build();

		return restTemplate.postForEntity(postUrl.toString(), r, String.class);
	}

	@Test
	public void testAddDuplicate() throws MalformedURLException {
		// Post restaurant
		Restaurant r = generateRestaurant(60);

		HttpUrl postUrl = new HttpUrl.Builder()
				.scheme("http")
				.host("localhost")
				.port(port)
				.addPathSegment("list")
				.addPathSegment(HungryList.ListType.FAVORITE.toString())
				.addPathSegment("restaurant")
				.addQueryParameter("userId", String.valueOf(cID))
				.build();

		HttpUrl postUrl2 = new HttpUrl.Builder()
				.scheme("http")
				.host("localhost")
				.port(port)
				.addPathSegment("list")
				.addPathSegment(HungryList.ListType.FAVORITE.toString())
				.addPathSegment("restaurant")
				.addQueryParameter("userId", String.valueOf(cID))
				.build();

		restTemplate.postForEntity(postUrl.toString(), r, String.class);
		restTemplate.postForEntity(postUrl2.toString(), r, String.class);


		HungryList favoritesList = getList(String.valueOf(cID), HungryList.ListType.FAVORITE);

		assertNotNull(favoritesList);
		assertEquals(favoritesList.getItems().size(), 1);
	}

	@Test
	public void testAddRecipe() {
		Recipe r = new Recipe(
				"id",
				"Fried Rice",
				"PHOTO_URL",
				5,
				10,
				new ArrayList<>(),
				"INSTRUCTIONS"
		);

		HttpUrl postUrl = new HttpUrl.Builder()
				.scheme("http")
				.host("localhost")
				.port(port)
				.addPathSegment("list")
				.addPathSegment(HungryList.ListType.FAVORITE.toString())
				.addPathSegment("recipe")
				.addQueryParameter("userId", String.valueOf(cID))
				.build();

		ResponseEntity<String> response = restTemplate.postForEntity(postUrl.toString(), r, String.class);

		assertThat(response).isNotNull();
		assertEquals(response.getStatusCodeValue(), 200);
	}

	@Test
	public void testDeleteRestaurant() throws MalformedURLException {
		// Post restaurant
		Restaurant r = generateRestaurant(360);

		ResponseEntity<String> response = addRestaurantToList(String.valueOf(cID), r, HungryList.ListType.FAVORITE.toString());

		// Delete just posted restaurant
		HttpUrl deleteURL = new HttpUrl.Builder()
				.scheme("http")
				.host("localhost")
				.port(port)
				.addPathSegment("list")
				.addPathSegment(HungryList.ListType.FAVORITE.toString())
				.addPathSegment("restaurant")
				.addQueryParameter("restaurantId", "12345id")
				.addQueryParameter("userId", String.valueOf(cID))
				.build();

		restTemplate.delete(deleteURL.toString());

		assertThat(response).isNotNull();
		assertEquals(response.getStatusCodeValue(), 200);
	}

	Restaurant generateRestaurant(int i) throws MalformedURLException {
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

	@Test
	public void testDeleteRecipe() {
		// Add recipe to favorite last
		Recipe r = new Recipe(
				"id",
				"Fried Rice",
				"PHOTO_URL",
				5,
				10,
				new ArrayList<>(),
				"INSTRUCTIONS"
		);

		HttpUrl postUrl = new HttpUrl.Builder()
				.scheme("http")
				.host("localhost")
				.port(port)
				.addPathSegment("list")
				.addPathSegment(HungryList.ListType.FAVORITE.toString())
				.addPathSegment("recipe")
				.addQueryParameter("userId", String.valueOf(cID))
				.build();

		ResponseEntity<String> response = restTemplate.postForEntity(postUrl.toString(), r, String.class);

		// Now delete that recipe from list
		HttpUrl deleteURL = new HttpUrl.Builder()
				.scheme("http")
				.host("localhost")
				.port(port)
				.addPathSegment("list")
				.addPathSegment(HungryList.ListType.FAVORITE.toString())
				.addPathSegment("recipe")
				.addQueryParameter("recipeId", "id")
				.addQueryParameter("userId", String.valueOf(cID))
				.build();

		restTemplate.delete(deleteURL.toString());

		assertThat(response).isNotNull();
		assertEquals(response.getStatusCodeValue(), 200);
	}

}

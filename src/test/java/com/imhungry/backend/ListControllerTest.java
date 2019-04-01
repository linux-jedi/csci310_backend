package com.imhungry.backend;

import com.google.maps.model.PriceLevel;
import com.imhungry.backend.model.User;
import com.imhungry.backend.model.UserLists;
import com.imhungry.backend.repository.UserListsRepository;
import com.imhungry.backend.repository.UserRepository;
import okhttp3.HttpUrl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.Assert.assertEquals;

/**
 * Created by calebthomas on 3/7/19.
 */

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
	public void invalidUserTest() {
		HttpUrl url = new HttpUrl.Builder()
				.scheme("http")
				.host("localhost")
				.port(port)
				.addPathSegment("list")
				.addQueryParameter("userId", "10000")
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

		assertEquals(favoritesList.getName(), HungryList.ListType.EXPLORE.toString());
		assertEquals(favoritesList.getRestaurants().size(), 0);
	}

	@Test
	public void getBlockList() {
		HttpUrl url = new HttpUrl.Builder()
				.scheme("http")
				.host("localhost")
				.port(port)
				.addPathSegment("list")
				.addPathSegment(HungryList.ListType.BLOCK.toString())
				.addQueryParameter("userId", String.valueOf(cID))
				.build();

		ResponseEntity<HungryList> responseEntity = restTemplate.getForEntity(url.toString(), HungryList.class);
		HungryList favoritesList = responseEntity.getBody();

		assertEquals(favoritesList.getName(), HungryList.ListType.BLOCK.toString());
		assertEquals(favoritesList.getRestaurants().size(), 0);
	}

	@Test
	public void getFavoriteList() {
		HttpUrl url = new HttpUrl.Builder()
				.scheme("http")
				.host("localhost")
				.port(port)
				.addPathSegment("list")
				.addPathSegment(HungryList.ListType.FAVORITE.toString())
				.addQueryParameter("userId", String.valueOf(cID))
				.build();

		ResponseEntity<HungryList> responseEntity = restTemplate.getForEntity(url.toString(), HungryList.class);
		HungryList favoritesList = responseEntity.getBody();

		assertEquals(favoritesList.getName(), HungryList.ListType.FAVORITE.toString());
		assertEquals(favoritesList.getRestaurants().size(), 0);
	}

	@Test
	public void getInvalidList() {
		HttpUrl url = new HttpUrl.Builder()
				.scheme("http")
				.host("localhost")
				.port(port)
				.addPathSegment("list")
				.addPathSegment("INVALID")
				.addQueryParameter("userId", String.valueOf(cID))
				.build();

		ResponseEntity<HungryList> responseEntity = restTemplate.getForEntity(url.toString(), HungryList.class);
		HungryList favoritesList = responseEntity.getBody();

		assertEquals(favoritesList.getName(), HungryList.ListType.FAVORITE.toString());
		assertEquals(favoritesList.getRestaurants().size(), 0);
	}

	@Test
	public void testAddRestaurant() throws MalformedURLException {
		// Post restaurant
		Restaurant r = new Restaurant(
				"12345id",
				"Panda Express",
				"12345 McClintock Avenue",
				"1-515-888-8888",
				new URL("http://www.pandaexpress.com"),
				5,
				PriceLevel.MODERATE,
				"9 minutes",
				9 * 60
		);

		HttpUrl postUrl = new HttpUrl.Builder()
				.scheme("http")
				.host("localhost")
				.port(port)
				.addPathSegment("list")
				.addPathSegment(HungryList.ListType.FAVORITE.toString())
				.addPathSegment("restaurant")
				.addQueryParameter("userId", String.valueOf(cID))
				.build();

		ResponseEntity<String> response = restTemplate.postForEntity(postUrl.toString(), r, String.class);

		assertThat(response).isNotNull();
		assertEquals(response.getStatusCodeValue(), 200);
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
		Restaurant r = new Restaurant(
				"12345id",
				"Panda Express",
				"12345 McClintock Avenue",
				"1-515-888-8888",
				new URL("http://www.pandaexpress.com"),
				5,
				PriceLevel.MODERATE,
				"9 minutes",
				9 * 360
		);

		HttpUrl postUrl = new HttpUrl.Builder()
				.scheme("http")
				.host("localhost")
				.port(port)
				.addPathSegment("list")
				.addPathSegment(HungryList.ListType.FAVORITE.toString())
				.addPathSegment("restaurant")
				.addQueryParameter("userId", String.valueOf(cID))
				.build();

		ResponseEntity<String> response = restTemplate.postForEntity(postUrl.toString(), r, String.class);

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

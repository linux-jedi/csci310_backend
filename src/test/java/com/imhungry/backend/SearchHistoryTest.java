package com.imhungry.backend;

import com.imhungry.backend.model.SearchQuery;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = "dev")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SearchHistoryTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	private String register() {
		return TestUtilityMethods.register(port, restTemplate);
	}

	@Test
	public void checkPersistenceTest() {
		String uid1 = register();
		String uid2 = register();

		// Add items to user 1
		addNewQueryToHistory("chinese", 1, 3, uid1);
		addNewQueryToHistory("chinese", 2, 3, uid1);
		addNewQueryToHistory("dummy", 3, 3, uid1);

		// Add items to user 2
		addNewQueryToHistory("burger", 1, 3, uid2);
		addNewQueryToHistory("dummy", 2, 3, uid2);

		// Check that user 1's values are correct
		assertEquals(2, getSearchHistory(uid1).length);

		// Check that user 2's values are correct
		assertEquals(1, getSearchHistory(uid2).length);
	}

	// Ensure that the most recently searched item is not included in the search history
	@Test
	public void checkLastNotIncluded() {
		String uid = register();

		// Search for two things
		addNewQueryToHistory("chinese", 5, 3, uid);
		addNewQueryToHistory("dummy", 5, 3, uid);

		SearchQuery[] searchHistory = getSearchHistory(uid);

		assertNotNull(searchHistory);

		// Show that the search history contains only one item
		assertEquals(searchHistory.length, 1);

		// Show that that item is not the one most recently searched
		assertEquals(searchHistory[0].getSearchTerm(), "chinese");


	}

	@Test
	public void addAndGetHistoryTest() {
		String uid = register();
		addNewQueryToHistory("chinese", 5, 3, uid);
		addNewQueryToHistory("dummy", 0, 0, uid);

		SearchQuery[] searchHistory = getSearchHistory(uid);

		assertNotNull(searchHistory);
		assertEquals(1, searchHistory.length);

		SearchQuery searchQuery = searchHistory[0];
		assertEquals("chinese", searchQuery.getSearchTerm());
		assertEquals(5, searchQuery.getAmount());
		assertEquals(3, searchQuery.getRadius());
		System.out.println(searchQuery.getCollageList().getClass());
		assertEquals("https://assets3.thrillist.com/v1/image/1864928/size/tmg-article_default_mobile.jpg",
				searchQuery.getCollageList()[0]);
	}

	@Test
	public void searchHistoryUniquenessTest() {
		String uid = register();
		addNewQueryToHistory("chinese", 5, 3, uid);
		addNewQueryToHistory("burger", 5, 3, uid);
		addNewQueryToHistory("chinese", 50, 3, uid);
		addNewQueryToHistory("chinese", 5, 1, uid);
		addNewQueryToHistory("dummy", 0, 0, uid);

		SearchQuery[] searchHistory = getSearchHistory(uid);

		assertNotNull(searchHistory);
		assertEquals(4, searchHistory.length);
	}

	@Test
	public void searchHistoryOrderTest() {
		String[] searches = {"burger", "chinese", "rice", "pizza", "dummy"};
		String[] expected = {"pizza", "rice", "chinese", "burger"};
		String uid = register();
		for (String search : searches) addNewQueryToHistory(search, 5, 10000, uid);

		SearchQuery[] searchHistory = getSearchHistory(uid);

		assertNotNull(searchHistory);
		assertEquals(4, searchHistory.length);

		for (int i = 0; i < 4; i++) {
			assertEquals(expected[i], searchHistory[i].getSearchTerm());
		}
	}

	@Test
	public void searchHistoryOrderWithRepeatsTest() {
		String[] searches = {"burger", "chinese", "rice", "chinese", "pizza", "dummy"};
		String[] expected = {"pizza", "chinese", "rice", "burger"};
		String uid = register();
		for (String search : searches) addNewQueryToHistory(search, 5, 10000, uid);

		SearchQuery[] searchHistory = getSearchHistory(uid);

		assertNotNull(searchHistory);
		assertEquals(4, searchHistory.length);
		for (int i = 0; i < 4; i++) {
			assertEquals(expected[i], searchHistory[i].getSearchTerm());
		}
	}

	// Test that you can obtain collage urls within search histories
	@Test
	public void collageSearchHistoryTest() {
		String[] searches = {"burger", "dummy"};
		String uid = register();
		for (String search : searches) addNewQueryToHistory(search, 5, 10000, uid);

		SearchQuery[] searchHistory = getSearchHistory(uid);

		assertNotNull(searchHistory);
		assertEquals(1, searchHistory.length);
		assertEquals(10, searchHistory[0].getCollageList().length);
	}


	private void addNewQueryToHistory(String searchTerm, int amount, int radius, String uid) {
		HttpUrl url = new HttpUrl.Builder()
				.scheme("http")
				.host("localhost")
				.port(port)
				.addPathSegment("search-history")
				.addQueryParameter("userid", uid)
				.addQueryParameter("searchterm", searchTerm)
				.addQueryParameter("amount", String.valueOf(amount))
				.addQueryParameter("radius", String.valueOf(radius))
				.build();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.TEXT_PLAIN);

		HttpEntity<String> entity = new HttpEntity<>("", headers);
		restTemplate.postForEntity(url.toString(), entity, String.class);
	}

	private SearchQuery[] getSearchHistory(String uid) {
		HttpUrl url;
		url = new HttpUrl.Builder()
				.scheme("http")
				.host("localhost")
				.port(port)
				.addPathSegment("search-history")
				.addQueryParameter("userid", uid)
				.build();

		ResponseEntity<SearchQuery[]> res = restTemplate.getForEntity(url.toString(), SearchQuery[].class);
		return res.getBody();
	}

}

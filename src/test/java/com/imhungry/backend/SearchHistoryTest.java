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

import static com.imhungry.backend.GroceryListTest.register;
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

	private String registerNewUser(String name) {
		return register(name, port, restTemplate);
	}

	@Test
	public void checkPersistenceTest() {
		String uid1 = registerNewUser("SearchHistoryTest.checkPersistence1");
		String uid2 = registerNewUser("SearchHistoryTest.checkPersistence2");

		addNewQueryToHistory("chinese", 1, 3, uid1);
		addNewQueryToHistory("chinese", 2, 3, uid1);
		addNewQueryToHistory("dummy", 3, 3, uid1);

		addNewQueryToHistory("burger", 1, 3, uid2);
		addNewQueryToHistory("dummy", 2, 3, uid2);

		assertEquals(2, getSearchHistory(uid1).length);
		assertEquals(1, getSearchHistory(uid2).length);
	}

	@Test
	public void checkLastNotIncluded() {
		String uid = registerNewUser("checkLastNotIncluded");
		addNewQueryToHistory("chinese", 5, 3, uid);

		SearchQuery[] searchHistory = getSearchHistory(uid);

		assertNotNull(searchHistory);
		assertEquals(searchHistory.length, 0);

	}

	@Test
	public void addAndGetHistoryTest() {
		String uid = registerNewUser("addAndGetHistoryTest");
		addNewQueryToHistory("chinese", 5, 3, uid);
		addNewQueryToHistory("dummy", 0, 0, uid);

		SearchQuery[] searchHistory = getSearchHistory(uid);

		assertNotNull(searchHistory);
		assertEquals(searchHistory.length, 1);

		SearchQuery searchQuery = searchHistory[0];
		assertEquals(searchQuery.getSearchTerm(), "chinese");
		assertEquals(searchQuery.getAmount(), 5);
		assertEquals(searchQuery.getRadius(), 3);
		System.out.println(searchQuery.getCollageList().getClass());
		assertEquals(searchQuery.getCollageList()[0],
				"https://assets3.thrillist.com/v1/image/1864928/size/tmg-article_default_mobile.jpg");
	}

	@Test
	public void searchHistoryUniquenessTest() {
		String uid = registerNewUser("searchHistoryUniquenessTest");
		addNewQueryToHistory("chinese", 5, 3, uid);
		addNewQueryToHistory("burger", 5, 3, uid);
		addNewQueryToHistory("chinese", 50, 3, uid);
		addNewQueryToHistory("chinese", 5, 1, uid);
		addNewQueryToHistory("dummy", 0, 0, uid);

		SearchQuery[] searchHistory = getSearchHistory(uid);

		assertNotNull(searchHistory);
		assertEquals(searchHistory.length, 4);
	}

	@Test
	public void searchHistoryOrderTest() {
		String[] searches = {"burger", "chinese", "rice", "pizza", "dummy"};
		String[] expected = {"pizza", "rice", "chinese", "burger"};
		String uid = registerNewUser("searchHistoryOrderTest");
		for (String search : searches) addNewQueryToHistory(search, 5, 10000, uid);

		SearchQuery[] searchHistory = getSearchHistory(uid);

		assertNotNull(searchHistory);
		assertEquals(searchHistory.length, 4);

		for (int i = 0; i < 4; i++) {
			assertEquals(expected[i], searchHistory[i].getSearchTerm());
		}
	}

	@Test
	public void searchHistoryOrderWithRepeatsTest() {
		String[] searches = {"burger", "chinese", "rice", "chinese", "pizza", "dummy"};
		String[] expected = {"pizza", "chinese", "rice", "burger"};
		String uid = registerNewUser("searchHistoryOrderWithRepeatsTest");
		for (String search : searches) addNewQueryToHistory(search, 5, 10000, uid);

		SearchQuery[] searchHistory = getSearchHistory(uid);

		assertNotNull(searchHistory);
		assertEquals(searchHistory.length, 4);
		for (int i = 0; i < 4; i++) {
			assertEquals(expected[i], searchHistory[i].getSearchTerm());
		}
	}

	@Test
	public void collageSearchHistoryTest() {
		String[] searches = {"burger", "dummy"};
		String uid = registerNewUser("collageSearchHistoryTest");
		for (String search : searches) addNewQueryToHistory(search, 5, 10000, uid);

		SearchQuery[] searchHistory = getSearchHistory(uid);

		assertNotNull(searchHistory);
		assertEquals(searchHistory.length, 1);
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

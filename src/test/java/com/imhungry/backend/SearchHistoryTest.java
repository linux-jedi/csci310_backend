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
import static org.hibernate.internal.util.collections.ArrayHelper.reverse;
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
	public void addAndGetHistoryTest() {
		String uid = registerNewUser("addAndGetHistoryTest");
		addNewQueryToHistory("chinese", 5, 10000, uid);

		SearchQuery[] searchHistory = getSearchHistory(uid);

		assertNotNull(searchHistory);
		assertEquals(searchHistory.length, 1);

		SearchQuery searchQuery = searchHistory[0];
		assertEquals(searchQuery.getSearchTerm(), "chinese");
		assertEquals(searchQuery.getAmount(), 5);
		assertEquals(searchQuery.getRadius(), 10000);

	}

	@Test
	public void searchHistoryUniquenessTest() {
		String uid = registerNewUser("searchHistoryUniquenessTest");
		addNewQueryToHistory("chinese", 5, 10000, uid);
		addNewQueryToHistory("burger", 5, 10000, uid);
		addNewQueryToHistory("chinese", 50, 10000, uid);
		addNewQueryToHistory("chinese", 5, 1000, uid);

		SearchQuery[] searchHistory = getSearchHistory(uid);

		assertNotNull(searchHistory);
		assertEquals(searchHistory.length, 4);
	}

	@Test
	public void searchHistoryOrderTest() {
		String[] searches = {"burger", "chinese", "rice", "pizza"};
		String[] expected = reverse(searches);
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
		String[] searches = {"burger", "chinese", "rice", "chinese", "pizza"};
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

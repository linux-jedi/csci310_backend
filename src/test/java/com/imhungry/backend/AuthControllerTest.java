package com.imhungry.backend;

import com.imhungry.backend.model.User;
import com.imhungry.backend.repository.UserListsRepository;
import com.imhungry.backend.repository.UserRepository;
import okhttp3.HttpUrl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by calebthomas on 3/7/19.
 */

@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = "dev")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerTest {

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

	@Test
	public void getRegistrationTest() {
		HttpUrl url = new HttpUrl.Builder()
				.scheme("http")
				.host("localhost")
				.port(port)
				.addPathSegment("register")
				.addQueryParameter("username", "test")
				.addQueryParameter("email", "test")
				.addQueryParameter("password", "test")
				.build();

		ResponseEntity<User> responseEntity = restTemplate.postForEntity(url.toString(), new HttpEntity<>(""), User.class);
		User user = responseEntity.getBody();
		assertEquals(user.getEmail(), "test");
		assertEquals(user.getUsername(), "test");
		assertNotNull(user.getId());
	}

	@Test
	public void getLoginTest() {
		HttpUrl url = new HttpUrl.Builder()
				.scheme("http")
				.host("localhost")
				.port(port)
				.addPathSegment("register")
				.addQueryParameter("username", "test2")
				.addQueryParameter("email", "test2")
				.addQueryParameter("password", "test2")
				.build();

		ResponseEntity<User> responseEntity = restTemplate.postForEntity(url.toString(), new HttpEntity<>(""), User.class);
		User register_user = responseEntity.getBody();

		url = new HttpUrl.Builder()
				.scheme("http")
				.host("localhost")
				.port(port)
				.addPathSegment("login")
				.addQueryParameter("username", "test2")
				.addQueryParameter("password", "test2")
				.build();

		ResponseEntity<User> loginResponseEntity = restTemplate.postForEntity(url.toString(), new HttpEntity<>(""), User.class);
		User login_user = loginResponseEntity.getBody();

		assertEquals(login_user.getEmail(), register_user.getEmail());
		assertEquals(login_user.getUsername(), register_user.getUsername());
		assertEquals(login_user.getId(), register_user.getId());

	}

	@Test
	public void getBadLoginTest() {
		HttpUrl url = new HttpUrl.Builder()
				.scheme("http")
				.host("localhost")
				.port(port)
				.addPathSegment("register")
				.addQueryParameter("username", "test2")
				.addQueryParameter("email", "test2")
				.addQueryParameter("password", "test2")
				.build();

		ResponseEntity<User> responseEntity = restTemplate.postForEntity(url.toString(), new HttpEntity<>(""), User.class);
		User register_user = responseEntity.getBody();

		url = new HttpUrl.Builder()
				.scheme("http")
				.host("localhost")
				.port(port)
				.addPathSegment("login")
				.addQueryParameter("username", "test2")
				.addQueryParameter("password", "test3")
				.build();

		ResponseEntity<User> loginResponseEntityBadPassword = restTemplate.postForEntity(url.toString(), new HttpEntity<>(""), User.class);
		User badPasswordUser = loginResponseEntityBadPassword.getBody();

		assertNull(badPasswordUser.getId());

		url = new HttpUrl.Builder()
				.scheme("http")
				.host("localhost")
				.port(port)
				.addPathSegment("login")
				.addQueryParameter("username", "test3")
				.addQueryParameter("password", "test2")
				.build();

		ResponseEntity<User> loginResponseEntityBadUser = restTemplate.postForEntity(url.toString(), new HttpEntity<>(""), User.class);
		User badUsernameUser = loginResponseEntityBadUser.getBody();

		assertNull(badUsernameUser.getId());



	}


}

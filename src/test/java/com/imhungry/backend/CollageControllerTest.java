package com.imhungry.backend;

import okhttp3.HttpUrl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URL;

import static org.junit.Assert.assertTrue;

/**
 * Created by calebthomas on 3/7/19.
 */

@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = "dev")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CollageControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testGetCollage() {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host("localhost")
                .port(port)
                .addPathSegment("collage")
                .addQueryParameter("searchTerm", "chinese")
                .build();

        ResponseEntity<URL[]> entity = restTemplate.getForEntity(url.toString(), URL[].class);
        URL[] collageImages = entity.getBody();

        assertTrue(collageImages.length > 1);
    }
}

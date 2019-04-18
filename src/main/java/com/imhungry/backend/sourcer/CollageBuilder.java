package com.imhungry.backend.sourcer;

import com.google.gson.*;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@CacheConfig(cacheNames={"imageUrls"})
public class CollageBuilder {

    @Value("${engine.id}")
    private String ENGINE_ID;

    @Value("${search.api.key}")
    private String SEARCH_API_KEY;

    @Cacheable
    public List<URL> getUrls(String searchTerm, int numImages) throws IOException {
        List<URL> imageUrls = new ArrayList<>();

        OkHttpClient client = new OkHttpClient();
        HttpUrl requestUrl = new HttpUrl.Builder()
                .scheme("https")
                .host("www.googleapis.com")
                .addPathSegment("customsearch")
                .addPathSegment("v1")
                .addQueryParameter("key", SEARCH_API_KEY)
                .addQueryParameter("cx", ENGINE_ID)
                .addQueryParameter("q", searchTerm)
                .addQueryParameter("fields", "items/link")
                .addQueryParameter("searchType", "image")
                .addQueryParameter("num", "10")
                .build();

        // Loop requests until we get desired images
        int resultsIndex = 1;
        while (imageUrls.size() < numImages) {
            // Build request
            HttpUrl pageRequestUrl = HttpUrl.parse(requestUrl.toString()).newBuilder()
                    .addQueryParameter("start", Integer.toString(resultsIndex))
                    .build();

            Request request = new Request.Builder()
                    .url(pageRequestUrl)
                    .get()
                    .build();

            // Execute request
            Response res = client.newCall(request).execute();

            // Parse JSON response
            JsonObject myResponse = new JsonParser().parse(res.body().charStream()).getAsJsonObject();
            JsonArray items = myResponse.getAsJsonArray("items");

            // loop through the images
            for (JsonElement item : items){
                JsonPrimitive image = item.getAsJsonObject().getAsJsonPrimitive("link");

                // Get image thumbnail URL
                URL link = new URL(image.getAsString());
                imageUrls.add(link);

                // finishes once images are downloaded
                if (imageUrls.size() == numImages) {
                    break;
                }
            }

            // For multiple google custom search requests
            resultsIndex += 10;
        }

        return imageUrls;
    }

}

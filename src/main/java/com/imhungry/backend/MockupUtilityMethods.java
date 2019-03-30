package com.imhungry.backend;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Profile("test")
@Configuration
public class MockupUtilityMethods {

	private static Gson gson = new Gson();

	public static Restaurant getSingleRestaurant() throws IOException {
		String jsonString = readFile("src/test/java/com/imhungry/backend/json/restaurant_result_chinese_5.json");
		List<Restaurant> restaurants = gson.fromJson(jsonString, new TypeToken<List<Restaurant>>() {}.getType());
		return restaurants.get(0);
	}

	public static List<Restaurant> getFiveChineseRestaurants() throws IOException {
		String jsonString = readFile("src/test/java/com/imhungry/backend/json/restaurant_result_chinese_5.json");
		return gson.fromJson(jsonString, new TypeToken<List<Restaurant>>() {}.getType());
	}

	public static Recipe getSingleRecipe() throws IOException{
		String jsonString = readFile("src/test/java/com/imhungry/backend/json/recipe_result_chinese_5.json");
		List<Recipe> recipes = gson.fromJson(jsonString, new TypeToken<List<Recipe>>() {}.getType());
		return recipes.get(1);
	}

	public static List<Recipe> getFiveChineseRecipes() throws IOException {
		String jsonString = readFile("src/test/java/com/imhungry/backend/json/recipe_result_chinese_5.json");
		return gson.fromJson(jsonString, new TypeToken<List<Recipe>>() {}.getType());
	}

	public static List<URL> getImageURLsChineseFood() throws IOException {
		String jsonString = readFile("src/test/java/com/imhungry/backend/json/image_result_chinese_5.json");
		List<String> stringies = gson.fromJson(jsonString, new TypeToken<List<String>>() {}.getType());
		List<URL> urls = new ArrayList<>();
		for (String s: stringies) {
			urls.add(new URL(s));
		}
		return urls;
	}



	// Read from File to String
	static String readFile(String path) throws IOException
	{
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded);
	}


}
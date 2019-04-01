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

	private static List<URL> getUrls(String s2) throws IOException {
		String jsonString = readFile(s2);
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

	public static List<Restaurant> getFiveChineseRestaurants() throws IOException {
		String jsonString = readFile("src/test/java/com/imhungry/backend/json/restaurant_result_chinese_5.json");
		return gson.fromJson(jsonString, new TypeToken<List<Restaurant>>() {}.getType());
	}

	public static List<Recipe> getFiveChineseRecipes() throws IOException {
		String jsonString = readFile("src/test/java/com/imhungry/backend/json/recipe_result_chinese_5.json");
		return gson.fromJson(jsonString, new TypeToken<List<Recipe>>() {}.getType());
	}

	public static List<Restaurant> getFiveBurgerRestaurants() throws IOException {
		String jsonString = readFile("src/test/java/com/imhungry/backend/json/restaurant_result_burger_5.json");
		return gson.fromJson(jsonString, new TypeToken<List<Restaurant>>() {}.getType());
	}

	public static List<Recipe> getFiveBurgerRecipes() throws IOException {
		String jsonString = readFile("src/test/java/com/imhungry/backend/json/recipe_result_burger_5.json");
		return gson.fromJson(jsonString, new TypeToken<List<Recipe>>() {}.getType());
	}

	public static List<URL> getImageURLsBurgerFood() throws IOException {
		return getUrls("src/test/java/com/imhungry/backend/json/image_result_burger_5.json");
	}

	public static List<URL> getImageURLsChineseFood() throws IOException {
		return getUrls("src/test/java/com/imhungry/backend/json/image_result_chinese_5.json");
	}


	public static Restaurant getNorthernCafe() throws IOException {
		String jsonString = readFile("src/test/java/com/imhungry/backend/json/restaurant_result_chinese_5.json");
		List<Restaurant> restaurants = gson.fromJson(jsonString, new TypeToken<List<Restaurant>>() {}.getType());
		return restaurants.get(0);
	}

	public static Restaurant getHabitBurger() throws IOException {
		String jsonString = readFile("src/test/java/com/imhungry/backend/json/restaurant_result_burger_5.json");
		List<Restaurant> restaurants = gson.fromJson(jsonString, new TypeToken<List<Restaurant>>() {}.getType());
		return restaurants.get(0);
	}

	public static Recipe getFriedRice() throws IOException{
		String jsonString = readFile("src/test/java/com/imhungry/backend/json/recipe_result_chinese_5.json");
		List<Recipe> recipes = gson.fromJson(jsonString, new TypeToken<List<Recipe>>() {}.getType());
		return recipes.get(1);
	}

	public static Recipe getAubgergineBurger() throws IOException {
		String jsonString = readFile("src/test/java/com/imhungry/backend/json/recipe_result_burger_5.json");
		List<Recipe> recipes = gson.fromJson(jsonString, new TypeToken<List<Recipe>>() {}.getType());
		return recipes.get(0);
	}
}
package com.imhungry.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imhungry.backend.spoonacular.BasicRecipe;
import com.imhungry.backend.spoonacular.DetailedRecipe;
import com.imhungry.backend.spoonacular.Ingredient;
import com.imhungry.backend.spoonacular.SearchRecipesResult;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by calebthomas on 2/28/19.
 */

public class RecipeSourcer {

    @Value("${RECIPE_API_KEY}")
    private static String API_KEY;

    public static List<Recipe> getRecipes(String keyword, int maxRecipes) {
        List<Recipe> recipes = new ArrayList<>();

        // Build request
        OkHttpClient client = new OkHttpClient();
        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("spoonacular-recipe-food-nutrition-v1.p.rapidapi.com")
                .addPathSegment("recipes")
                .addPathSegment("search")
                .addQueryParameter("query", keyword)
                .addQueryParameter("number", Integer.toString(maxRecipes))
                .build();

        Request req = new Request.Builder()
                .addHeader("x-rapidapi-key", API_KEY)
                .url(url)
                .get()
                .build();

        // Make Request to spoonacular API
        Response res = null;
        SearchRecipesResult results = null;
        try {
            res = client.newCall(req).execute();

            ObjectMapper mapper = new ObjectMapper();
            results = mapper.readValue(res.body().byteStream(), SearchRecipesResult.class);
        } catch(Exception e) {
            e.printStackTrace();
        }

        for(BasicRecipe r : results.getResults()) {
            recipes.add(new Recipe(
                    r.getId().toString(),
                    r.getTitle(),
                    "PHOTO_URL_PLACEHOLDER",
                    r.getReadyInMinutes().toString(),
                    "COOK_TIME_PLACEHOLDER",
                    new ArrayList<String>(),
                    "INSTRUCTION_PLACEHOLDER"
            ));
        }

        return recipes;
    }

    public static Recipe getRecipe(String recipeId) {
        OkHttpClient client = new OkHttpClient();
        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("spoonacular-recipe-food-nutrition-v1.p.rapidapi.com")
                .addPathSegment("recipes")
                .addEncodedPathSegment(recipeId)
                .addPathSegment("information")
                .build();

        Request req = new Request.Builder()
                .addHeader("x-rapidapi-key", API_KEY)
                .url(url)
                .get()
                .build();


        Response res = null;
        DetailedRecipe recipe = null;

        try{
            res = client.newCall(req).execute();

            ObjectMapper mapper = new ObjectMapper();
            recipe = mapper.readValue(res.body().byteStream(), DetailedRecipe.class);

        } catch(Exception e) {
            e.printStackTrace();
        }

        // Build ingredients from DetailedRecipe class
        List<String> ingredients = new ArrayList<>();
        for(Ingredient i: recipe.getExtendedIngredients()) {
            ingredients.add(i.getOriginalString());
        }

        return new Recipe(
                recipe.getId().toString(),
                recipe.getTitle(),
                recipe.getImage(),
                recipe.getPreperationMinutes().toString(),
                recipe.getCookingMinutes().toString(),
                ingredients,
                recipe.getInstructions()
        );
    }

}

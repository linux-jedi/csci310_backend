package com.imhungry.backend.sourcer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imhungry.backend.data.Recipe;
import com.imhungry.backend.spoonacular.BasicRecipe;
import com.imhungry.backend.spoonacular.DetailedRecipe;
import com.imhungry.backend.spoonacular.Ingredient;
import com.imhungry.backend.spoonacular.SearchRecipesResult;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.sort;

public class RecipeSourcer {

    @Value("${recipe.api.key}")
    private String API_KEY;

    public List<Recipe> searchRecipes(String keyword, int maxRecipes) throws Exception {
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
        res = client.newCall(req).execute();

        ObjectMapper mapper = new ObjectMapper();
        results = mapper.readValue(res.body().byteStream(), SearchRecipesResult.class);

        for(BasicRecipe r : results.getResults()) {
            recipes.add(new Recipe(
                    r.getId().toString(),
                    r.getTitle(),
                    "PHOTO_URL_PLACEHOLDER",
                    r.getReadyInMinutes(),
                    10,
                    new ArrayList<String>(),
                    "INSTRUCTION_PLACEHOLDER"
            ));
        }

        sort(recipes);
        return recipes;
    }

    public Recipe getRecipe(String recipeId) throws Exception {
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

        res = client.newCall(req).execute();

        ObjectMapper mapper = new ObjectMapper();
        recipe = mapper.readValue(res.body().byteStream(), DetailedRecipe.class);

        // Build ingredients from DetailedRecipe class
        List<String> ingredients = new ArrayList<>();
        for(Ingredient i: recipe.getExtendedIngredients()) {
            ingredients.add(i.getOriginalString());
        }

        return new Recipe(
                recipe.getId().toString(),
                recipe.getTitle(),
                recipe.getImage(),
                recipe.getPreperationMinutes(),
                recipe.getCookingMinutes(),
                ingredients,
                recipe.getInstructions()
        );
    }

}

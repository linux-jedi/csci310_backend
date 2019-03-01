package com.imhungry.backend.spoonacular;

import lombok.Data;

import java.util.List;

/**
 * Created by calebthomas on 2/23/19.
 */
@Data
public class DetailedRecipe {

    private Boolean vegetarian;

    private Boolean vegan;

    private Boolean glutenFree;

    private Boolean dairyFree;

    private Boolean veryHealthy;

    private Boolean cheap;

    private Boolean veryPopular;

    private Boolean sustainable;

    private Integer weightWatcherSmartPoints;

    private String gaps;

    private Boolean lowFodmap;

    private Boolean ketogenic;

    private Boolean whole30;

    private Integer servings;

    private String sourceUrl;

    private String spoonacularSourceUrl;

    private Integer aggregateLikes;

    private String creditText;

    private String sourceName;

    private List<Ingredient> extendedIngredients;

    private Integer id;

    private String title;

    private Integer readyInMinutes;

    private String image;

    private String instructions;
}

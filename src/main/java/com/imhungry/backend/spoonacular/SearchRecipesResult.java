package com.imhungry.backend.spoonacular;

import lombok.Data;

import java.util.List;

/**
 * Created by calebthomas on 2/23/19.
 */

@Data
public class SearchRecipesResult {

    private List<BasicRecipe> recipes;

    private String baseUri;

    private Integer offset;

    private Integer number;

    private Integer totalResults;

    private Integer processingTimeMs;

    private Integer expires;

    private Boolean isStale;

}

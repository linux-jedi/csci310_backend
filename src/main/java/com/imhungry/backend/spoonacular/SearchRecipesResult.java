package com.imhungry.backend.spoonacular;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * Created by calebthomas on 2/23/19.
 */

@Data
@JsonIgnoreProperties(ignoreUnknown =  true)
public class SearchRecipesResult {

    private List<BasicRecipe> results;

    private String baseUri;

    private Integer offset;

    private Integer number;

    private Integer totalResults;

    private Integer processingTimeMs;

    private Long expires;

    private Boolean isStale;

}

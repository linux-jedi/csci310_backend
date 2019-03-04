package com.imhungry.backend.spoonacular;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * Created by calebthomas on 2/23/19.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown =  true)
public class BasicRecipe {

    private Long id;

    private Integer servings;

    private String title;

    private Integer readyInMinutes;

    private String image;

    private List<String> imageUrls;
}

package com.imhungry.backend.spoonacular;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

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

package com.imhungry.backend.spoonacular;

import lombok.Data;

import java.util.List;

/**
 * Created by calebthomas on 2/23/19.
 */
@Data
public class BasicRecipe {

    private Integer id;

    private String title;

    private Integer readyInMinutes;

    private String image;

    private List<String> imageUrls;
}

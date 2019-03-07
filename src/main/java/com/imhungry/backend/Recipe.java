package com.imhungry.backend;

import lombok.Data;
import lombok.NonNull;

import java.util.List;

/**
 * Created by calebthomas on 2/22/19.
 */
@Data
public class Recipe {

    @NonNull
    private final String id;

    @NonNull
    private final String title;

    private final String photoUrl;

    private final Integer prepTime;

    private final Integer cookTime;

    private final List<String> ingredients;

    private final String instructions;
}

package com.imhungry.backend;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

/**
 * Created by calebthomas on 2/22/19.
 */
@Data
@AllArgsConstructor
public class Recipe {

    private final String id;

    private final String title;

    private final String photoUrl;

    private final Integer prepTime;

    private final Integer cookTime;

    private final List<String> ingredients;

    private final String instructions;
}

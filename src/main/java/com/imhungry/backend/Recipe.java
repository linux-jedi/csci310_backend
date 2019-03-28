package com.imhungry.backend;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Created by calebthomas on 2/22/19.
 */
@Data
@AllArgsConstructor
public class Recipe implements Comparable {

    private final String id;

    private final String title;

    private final String photoUrl;

    private final Integer prepTime;

    private final Integer cookTime;

    private final List<String> ingredients;

    private final String instructions;

    @Override
    public int compareTo(Object o) {
        Recipe r = (Recipe) o;
        return this.getPrepTime() - r.getPrepTime();
    }
}

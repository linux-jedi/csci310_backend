package com.imhungry.backend.spoonacular;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown =  true)
public class Ingredient {

    private Integer id;

    private String aisle;

    private String image;

    private String name;

    private String amount;

    private String unit;

    private String unitShort;

    private String unitLong;

    private String originalString;
}

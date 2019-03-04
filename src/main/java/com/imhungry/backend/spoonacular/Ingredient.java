package com.imhungry.backend.spoonacular;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by calebthomas on 2/23/19.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown =  true)
public class Ingredient {
//    "id":1022009
//            "aisle":"Ethnic Foods"
//            "image":"https://spoonacular.com/cdn/ingredients_100x100/chili-powder.jpg"
//            "name":"ancho chile powder"
//            "amount":1.5
//            "unit":"teaspoons"
//            "unitShort":"t"
//            "unitLong":"teaspoons"
//            "originalString":"1 1/2 teaspoons chipotle chile powder or ancho chile powder"

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

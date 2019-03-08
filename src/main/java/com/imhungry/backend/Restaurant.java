package com.imhungry.backend;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.maps.model.PriceLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.net.URL;

/**
 * Created by calebthomas on 2/22/19.
 */
@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Restaurant {

    private final String id;

    private final String name;

    private final String address;

    private final String phoneNumber;

    private final URL websiteUrl;

    private final float rating;

    private final PriceLevel priceRating;

    private final String distance;
}

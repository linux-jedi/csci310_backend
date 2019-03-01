package com.imhungry.backend;

import com.google.maps.model.PriceLevel;
import lombok.Data;
import lombok.NonNull;

import java.net.URL;

/**
 * Created by calebthomas on 2/22/19.
 */
@Data
public class Restaurant {

    @NonNull
    private final String id;

    @NonNull
    private final String name;

    private final String address;

    private final String phoneNumber;

    private final URL websiteUrl;

    private final float rating;

    private final PriceLevel priceRating;

    private final String distance;
}

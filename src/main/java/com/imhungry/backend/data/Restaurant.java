package com.imhungry.backend.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.maps.model.PriceLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.net.URL;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Restaurant implements Comparable, ListItem {

    private final String id;

    private final String name;

    private final String address;

    private final String phoneNumber;

    private final URL websiteUrl;

    private final float rating;

    private final PriceLevel priceRating;

    private final String distance;

    private final Integer time;

    @Override
    public int compareTo(Object o) {
        Restaurant r = (Restaurant) o;
        return this.getTime() - r.getTime();
    }

}

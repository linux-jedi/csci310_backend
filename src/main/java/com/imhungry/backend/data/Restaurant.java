package com.imhungry.backend.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.maps.model.PriceLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.net.URL;

/**
 * Created by calebthomas on 2/22/19.
 */
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
        Integer this_t = this.getTime();
        Integer r_t = r.getTime();
        return this_t - r_t;
    }

}

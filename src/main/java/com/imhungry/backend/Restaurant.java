package com.imhungry.backend;

import lombok.Data;
import lombok.NonNull;

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

    private final String websiteUrl;
}

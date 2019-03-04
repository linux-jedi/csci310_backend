package com.imhungry.backend;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by calebthomas on 3/1/19.
 */
public class ListManager {

    private List<HungryList> hungryLists;

    public ListManager() {
        hungryLists = new ArrayList<>();
        hungryLists.add(new HungryList("Favorites"));
        hungryLists.add(new HungryList("Explore"));
        hungryLists.add(new HungryList("Block"));
    }
}

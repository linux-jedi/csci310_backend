package com.imhungry.backend;

import com.google.maps.model.PriceLevel;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by calebthomas on 3/7/19.
 */
@ActiveProfiles(profiles = "dev")
public class HungryListTest {

    @Test
    public void testRemoveRestaurant() throws MalformedURLException {
        HungryList list = new HungryList(HungryList.ListType.FAVORITE.toString());

        Restaurant r = new Restaurant(
                "12345id",
                "Panda Express",
                "12345 McClintock Avenue",
                "1-515-888-8888",
                new URL("http://www.pandaexpress.com"),
                5,
                PriceLevel.MODERATE,
                "9 minutes",
                9 * 360
        );

        list.addRestaurant(r);
        assertFalse(list.getRestaurants().isEmpty());

        list.removeRestaurant(r.getId());
        assertTrue(list.getRestaurants().isEmpty());
    }

    @Test
    public void testRemoveRecipe()  {
        HungryList list = new HungryList(HungryList.ListType.FAVORITE.toString());

        Recipe r = new Recipe(
                "id",
                "Fried Rice",
                "PHOTO_URL",
                5,
                10,
                new ArrayList<>(),
                "INSTRUCTIONS"
        );

        list.addRecipe(r);
        assertFalse(list.getRecipes().isEmpty());

        list.removeRecipe(r.getId());
        assertTrue(list.getRecipes().isEmpty());
    }
}

package com.imhungry.backend;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by calebthomas on 2/28/19.
 */

@RestController
@RequestMapping("/list")
public class ListController {

    @Autowired
    private ListManager listManager;

    @GetMapping
    List<HungryList> getLists() {
        return listManager.getHungryLists();
    }

    @GetMapping(value = "/{listName}")
    public HungryList getList(@PathVariable String listName) {
        return selectList(listName);
    }

    @PostMapping(value = "/{listName}/restaurant")
    public void addRestaurant(@PathVariable String listName,
            @RequestBody Restaurant newRestaurant) {
        selectList(listName).addRestaurant(newRestaurant);
    }

    @PostMapping(value = "/{listName}/recipe")
    public void addRecipe(@PathVariable String listName,
                              @RequestBody Recipe newRecipe) {
        selectList(listName).addRecipe(newRecipe);
    }

    @DeleteMapping(value = "/{listName}/restaurant")
    public void removeRestaurant(@PathVariable String listName,
                                 @RequestParam(value = "restaurantId") String restaurantId) {
        selectList(listName).removeRestaurant(restaurantId);
    }

    @DeleteMapping(value = "/{listName}/recipe")
    public void removeRecipe(@PathVariable String listName,
                             @RequestParam(value = "recipeId") String recipeId) {
        selectList(listName).removeRecipe(recipeId);
    }

    private HungryList selectList(String listName) {
        switch(listName) {
            case "FAVORITE":
                return listManager.getHungryLists().get(0);
            case "EXPLORE":
                return listManager.getHungryLists().get(1);
            case "BLOCK":
                return listManager.getHungryLists().get(2);
        }
        return listManager.getHungryLists().get(0);
    }
}

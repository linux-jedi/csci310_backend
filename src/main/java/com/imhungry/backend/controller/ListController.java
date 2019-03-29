package com.imhungry.backend.controller;

import java.util.List;

import com.imhungry.backend.HungryList;
import com.imhungry.backend.UserListsJsonWrapper;
import com.imhungry.backend.Recipe;
import com.imhungry.backend.Restaurant;
import com.imhungry.backend.exception.UserListsNotFoundException;
import com.imhungry.backend.model.UserLists;
import com.imhungry.backend.repository.UserListsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by calebthomas on 2/28/19.
 */

@RestController
@CrossOrigin
@RequestMapping("/list")
public class ListController {

    @Autowired
    private UserListsJsonWrapper userListsJsonWrapper;

    @Autowired
    private UserListsRepository userListsRepository;

    @GetMapping
    List<HungryList> getLists(@RequestParam("userId") String userId) {
        // Get List object from database
        UserLists userLists = getUserLists(userId);

        // return lists
        return userLists.getUserListsJsonWrapper().getHungryLists();
    }

    @GetMapping(value = "/{listName}")
    public HungryList getList(@RequestParam("userId") String userId,
                              @PathVariable String listName) {

        // Get List object from database
        UserLists userLists = getUserLists(userId);

        // Choose correct list
        return selectList(userLists.getUserListsJsonWrapper(), listName);
    }

    @PostMapping(value = "/{listName}/restaurant")
    // TODO: add userId parameter
    public void addRestaurant(@PathVariable String listName,
                              @RequestParam("userId") String userId,
                              @RequestBody Restaurant newRestaurant) {
        // Get List object from database
        UserLists userLists = getUserLists(userId);

        // Add a restaurant
        selectList(userLists.getUserListsJsonWrapper(), listName).addRestaurant(newRestaurant);
        userListsRepository.flush();
    }

    @PostMapping(value = "/{listName}/recipe")
    public void addRecipe(@PathVariable String listName,
                          @RequestParam("userId") String userId,
                          @RequestBody Recipe newRecipe) {
        // Get List object from database
        UserLists userLists = getUserLists(userId);

        // Add a restaurant
        selectList(userLists.getUserListsJsonWrapper(), listName).addRecipe(newRecipe);
        userListsRepository.flush();
    }

    @DeleteMapping(value = "/{listName}/restaurant")
    public void removeRestaurant(@PathVariable String listName,
                                 @RequestParam("userId") String userId,
                                 @RequestParam(value = "restaurantId") String restaurantId) {
        // Get List object from database
        UserLists userLists = getUserLists(userId);

        // Remove restaurant
        selectList(userLists.getUserListsJsonWrapper(), listName).removeRestaurant(restaurantId);
        userListsRepository.flush();
    }

    @DeleteMapping(value = "/{listName}/recipe")
    // TODO: add userId parameter// TODO: add userId parameter
    public void removeRecipe(@PathVariable String listName,
                             @RequestParam("userId") String userId,
                             @RequestParam(value = "recipeId") String recipeId) {
        // Get List object from database
        UserLists userLists = getUserLists(userId);

        // Remove recipe
        selectList(userLists.getUserListsJsonWrapper(), listName).removeRecipe(recipeId);
        userListsRepository.flush();
    }

    private HungryList selectList(UserListsJsonWrapper listWrapper,
                                  String listName) {
        switch(listName) {
            case "FAVORITE":
                return listWrapper.getHungryLists().get(0);
            case "EXPLORE":
                return listWrapper.getHungryLists().get(1);
            case "BLOCK":
                return listWrapper.getHungryLists().get(2);
        }
        return listWrapper.getHungryLists().get(0);
    }

    private UserLists getUserLists(String userId) {
        // Convert userId to long
        Long userIdLong = Long.parseLong(userId);

        // Check that list exists
        if(!listExists(userIdLong)) {
            throw new UserListsNotFoundException(userIdLong);
        }

        // Get List object from database
        return userListsRepository.findByUserId(userIdLong).get();
    }

    /**
     * Used to check if list exists in database without
     * throwing an exception
     *
     * @param userId String representation of username
     * @return True if list associated with userId already exists in db
     */
    private boolean listExists(Long userId) {
        return userListsRepository.findByUserId(userId)
                .isPresent();
    }
}

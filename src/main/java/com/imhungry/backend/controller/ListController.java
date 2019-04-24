package com.imhungry.backend.controller;

import com.imhungry.backend.data.HungryList;
import com.imhungry.backend.data.Recipe;
import com.imhungry.backend.data.Restaurant;
import com.imhungry.backend.exception.UserListsNotFoundException;
import com.imhungry.backend.model.UserLists;
import com.imhungry.backend.repository.UserListsRepository;
import com.imhungry.backend.utils.UserListsJsonWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@CrossOrigin
@RequestMapping("/list")
public class ListController {

    @NotNull
    private final UserListsRepository userListsRepository;

    @Autowired
    public ListController(UserListsRepository userListsRepository) {
        this.userListsRepository = userListsRepository;
    }

    @GetMapping(value = "/{listName}")
    public HungryList getList(@RequestParam("userId") String userId,
                              @PathVariable String listName) {

        // Get List object from database
        UserLists userLists = getUserLists(userId);

        // Choose correct list
        return selectList(userLists.getUserListsJsonWrapper(), listName);
    }

    @PutMapping(value = "/{listName}")
    public void updateList(@RequestParam("userId") String userId,
                           @PathVariable String listName,
                           @RequestBody HungryList updatedList) {

        // Get list object from the database
        UserLists userLists = getUserLists(userId);

        switch(listName) {
            case "FAVORITE":
                userLists.getUserListsJsonWrapper().getHungryLists().set(0, updatedList);
                break;
            case "EXPLORE":
                userLists.getUserListsJsonWrapper().getHungryLists().set(1, updatedList);
                break;
            case "BLOCK":
                userLists.getUserListsJsonWrapper().getHungryLists().set(2, updatedList);
                break;
            default:
                return;
        }

        userListsRepository.saveAndFlush(userLists);
    }

    @PostMapping(value = "/{listName}/restaurant")
    public void addRestaurant(@PathVariable String listName,
                              @RequestParam("userId") String userId,
                              @RequestBody Restaurant newRestaurant) {
        // Get List object from database
        UserLists userLists = getUserLists(userId);

        // Add a restaurant
        selectList(userLists.getUserListsJsonWrapper(), listName).addItem(newRestaurant);
        userListsRepository.flush();
    }

    @PostMapping(value = "/{listName}/recipe")
    public void addRecipe(@PathVariable String listName,
                          @RequestParam("userId") String userId,
                          @RequestBody Recipe newRecipe) {
        // Get List object from database
        UserLists userLists = getUserLists(userId);

        // Add a restaurant
        selectList(userLists.getUserListsJsonWrapper(), listName).addItem(newRecipe);
        userListsRepository.flush();
    }

    @DeleteMapping(value = "/{listName}/restaurant")
    public void removeRestaurant(@PathVariable String listName,
                                 @RequestParam("userId") String userId,
                                 @RequestParam(value = "restaurantId") String restaurantId) {
        // Get List object from database
        UserLists userLists = getUserLists(userId);

        // Remove restaurant
        selectList(userLists.getUserListsJsonWrapper(), listName).removeItem(restaurantId);
        userListsRepository.flush();
    }

    @DeleteMapping(value = "/{listName}/recipe")
    public void removeRecipe(@PathVariable String listName,
                             @RequestParam("userId") String userId,
                             @RequestParam(value = "recipeId") String recipeId) {
        // Get List object from database
        UserLists userLists = getUserLists(userId);

        // Remove recipe
        selectList(userLists.getUserListsJsonWrapper(), listName).removeItem(recipeId);
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
            default:
                return null;
        }
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

    private boolean listExists(Long userId) {
        return userListsRepository.findByUserId(userId)
                .isPresent();
    }
}

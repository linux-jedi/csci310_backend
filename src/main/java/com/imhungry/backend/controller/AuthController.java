package com.imhungry.backend.controller;

import com.imhungry.backend.exception.EmailAlreadyExistsException;
import com.imhungry.backend.utils.UserListsJsonWrapper;
import com.imhungry.backend.exception.UserAlreadyExistsException;
import com.imhungry.backend.exception.UserNotFoundException;
import com.imhungry.backend.model.User;
import com.imhungry.backend.model.UserLists;
import com.imhungry.backend.repository.UserListsRepository;
import com.imhungry.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by calebthomas on 3/26/19.
 */
@RestController
@CrossOrigin
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    UserListsRepository userListsRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping("/login")
    public User login(@RequestParam("username") String username,
                      @RequestParam("password") String password) {

        // Hash password
        passwordEncoder.encode(password);

        // get user from database
        validateUserExists(username);
        User user = userRepository.findByUsername(username).get();

        // Check if user exists
        if(passwordEncoder.matches(password, user.getPassword())) {
            return user;
        } else {
            throw new UserNotFoundException(username);
        }
    }

    @PostMapping("/register")
    public User register(@RequestParam("username") String username,
                         @RequestParam("email") String email,
                         @RequestParam("password") String password) {

        // Check if username already exists
        if(userExists(username)) {
            throw new UserAlreadyExistsException(username);
        }

        if (userEmailExists(email)) {
            throw new EmailAlreadyExistsException(email);
        }

        // Hash password
        String hashedPassword = passwordEncoder.encode(password);

        // Create new user and user list
        User user = new User();
        user.setUsername(username);
        user.setPassword(hashedPassword);
        user.setEmail(email);
        user = userRepository.saveAndFlush(user);

        UserLists lists = new UserLists();
        lists.setUserId(user.getId());
        lists.setUserListsJsonWrapper(new UserListsJsonWrapper());
        userListsRepository.save(lists);

        return user;
    }

    private boolean userExists(String username) {
        return userRepository.findByUsername(username)
                .isPresent();
    }

    private boolean userEmailExists(String email) {
        return userRepository.findByEmail(email)
                .isPresent();
    }

    private void validateUserExists(String username) {
        if(!userExists(username)) {
            throw new UserNotFoundException(username);
        }
    }

}

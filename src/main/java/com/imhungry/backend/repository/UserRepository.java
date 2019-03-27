package com.imhungry.backend.repository;

import com.imhungry.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

/**
 * Created by calebthomas on 3/25/19.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(long id);

    Collection<User> findByUsernameAndPassword(String username, String password);

    Optional<User> findByUsername(String username);
}

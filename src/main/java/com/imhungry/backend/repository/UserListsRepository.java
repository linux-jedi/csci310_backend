package com.imhungry.backend.repository;

import com.imhungry.backend.model.UserLists;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserListsRepository extends JpaRepository<UserLists, Long> {

    /* Methods that will be implemented by Hibernate ORM */
    Optional<UserLists> findByUserId(Long userId);
}

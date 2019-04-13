package com.imhungry.backend.repository;

import com.imhungry.backend.model.SearchQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface SearchHistoryRepository extends JpaRepository<SearchQuery, Long> {

	Collection<SearchQuery> findAllByUserIdOrderByUpdatedAtDesc(long userId);

	Optional<SearchQuery> findByUserIdAndSearchTermAndAmountAndRadius
			(long userId, String searchTerm, int amount, int radius);

}

package com.imhungry.backend.repository;

import com.imhungry.backend.model.SearchQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface SearchHistoryRepository extends JpaRepository<SearchQuery, Long> {

	Collection<SearchQuery> findAllByUserIdOrderByUpdatedAtDesc(long userId);

	@Modifying
	@Query(value = "update searches s set s.userId = :uid where s.id = :id", nativeQuery = true)
	void updateSearchHistory (@Param("uid") Long uid,
							  @Param("id") Long id);

	Optional<SearchQuery> findByUserIdAndSearchTermAndAmountAndRadius
			(long userId, String searchTerm, int amount, int radius);

	void deleteById(long id);

}

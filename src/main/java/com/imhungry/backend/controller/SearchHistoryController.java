package com.imhungry.backend.controller;

import com.imhungry.backend.model.SearchQuery;
import com.imhungry.backend.repository.SearchHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/search-history")
public class SearchHistoryController {

	@Autowired
	SearchHistoryRepository searchHistoryRespository;

	@GetMapping
	public List<SearchQuery> getSearchHistory(
			@RequestParam(value = "userid") String userId) {

		// Get a user's search history
		long userIdLong = Long.parseLong(userId);
		return (List<SearchQuery>) searchHistoryRespository.findAllByUserIdOrderByUpdatedAtDesc(userIdLong);
	}

	@PostMapping
	public void postNewSearch(@RequestParam(value = "userid") String userId,
							  @RequestParam(value = "searchterm") String searchTerm,
							  @RequestParam(value = "amount") String amount,
							  @RequestParam(value = "radius") String radius) {

		long userIdLong = Long.parseLong(userId);
		int amountInt = Integer.parseInt(amount);
		int radiusInt = Integer.parseInt(radius);

		Optional<SearchQuery> searchQueryOption = searchHistoryRespository.findByUserIdAndSearchTermAndAmountAndRadius(
				userIdLong,
				searchTerm,
				amountInt,
				radiusInt);

		// Check if a search query already exists.
		// If it does, delete it (most recently used functionality for history)
		if (searchQueryOption.isPresent()) {
			Long id = searchQueryOption.get().getId();
			searchHistoryRespository.deleteById(id);
		}

		// Create and add a new query in the db
		SearchQuery searchQuery = new SearchQuery();
		searchQuery.setAmount(amountInt);
		searchQuery.setRadius(radiusInt);
		searchQuery.setUserId(userIdLong);
		searchQuery.setSearchTerm(searchTerm);

		searchHistoryRespository.saveAndFlush(searchQuery);
	}
}

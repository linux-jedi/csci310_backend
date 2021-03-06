package com.imhungry.backend.controller;

import com.imhungry.backend.model.SearchQuery;
import com.imhungry.backend.repository.SearchHistoryRepository;
import com.imhungry.backend.sourcer.CollageBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/search-history")
public class SearchHistoryController {

	@NotNull
	final SearchHistoryRepository searchHistoryRespository;

	@NotNull
	final CollageBuilder collageBuilder;

	@Autowired
	public SearchHistoryController(SearchHistoryRepository searchHistoryRespository, CollageBuilder collageBuilder) {
		this.searchHistoryRespository = searchHistoryRespository;
		this.collageBuilder = collageBuilder;
	}

	@GetMapping
	public List<SearchQuery> getSearchHistory(
			@RequestParam(value = "userid") String userId) {

		// Get a user's search history
		long userIdLong = Long.parseLong(userId);
		List<SearchQuery> retList = ((List<SearchQuery>) searchHistoryRespository.findAllByUserIdOrderByUpdatedAtDesc(userIdLong));
		retList.remove(0);
		return retList;
	}

	@PostMapping
	public void postNewSearch(@RequestParam(value = "userid") String userId,
							  @RequestParam(value = "searchterm") String searchTerm,
							  @RequestParam(value = "amount") String amount,
							  @RequestParam(value = "radius") String radius) throws IOException {

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

		String[] collageArray = collageBuilder.getUrls(searchTerm + " food", 10)
				.stream()
				.map(URL::toString).toArray(String[]::new);

		// Create and add a new query in the db
		SearchQuery searchQuery = new SearchQuery();
		searchQuery.setAmount(amountInt);
		searchQuery.setRadius(radiusInt);
		searchQuery.setUserId(userIdLong);
		searchQuery.setSearchTerm(searchTerm);
		searchQuery.setCollageList(collageArray);

		searchHistoryRespository.saveAndFlush(searchQuery);
	}
}

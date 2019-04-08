package com.imhungry.backend.controller;

import com.imhungry.backend.model.SearchQuery;
import com.imhungry.backend.repository.SearchHistoryRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/search-history")
public class SearchHistoryController {

    @Autowired
    SearchHistoryRespository searchHistoryRespository;

    @GetMapping
    public List<SearchQuery> getSearchHistory(
            @RequestParam(value = "userid") String userId) {

        long userIdLong = Long.parseLong(userId);
        return (List<SearchQuery>) searchHistoryRespository.findAllByUserIdOrderByUpdatedAtDesc(userIdLong);
    }

    @PostMapping
    public void postNewSearch(
            @RequestParam(value = "userid") String userId,
            @RequestParam(value = "searchterm") String searchTerm,
            @RequestParam(value = "amount") String amount,
            @RequestParam(value = "radius") String radius) {

        long userIdLong = Long.parseLong(userId);
        int amountInt = Integer.parseInt(amount);
        int radiusInt = Integer.parseInt(radius);

        Optional<SearchQuery> searchQueryOption = searchHistoryRespository.findByUserIdAndSearchTermAndAmountAndRadius(userIdLong,
                searchTerm,
                amountInt,
                radiusInt);

        if (searchQueryOption.isPresent()) {
            Long id = searchQueryOption.get().getId();
            Long uid = searchQueryOption.get().getId();
            searchHistoryRespository.updateSearchHistory(uid, id);
            return;
        }

        SearchQuery searchQuery = new SearchQuery();
        searchQuery.setAmount(amountInt);
        searchQuery.setRadius(radiusInt);
        searchQuery.setUserId(userIdLong);
        searchQuery.setSearchTerm(searchTerm);

        searchHistoryRespository.saveAndFlush(searchQuery);


    }
}

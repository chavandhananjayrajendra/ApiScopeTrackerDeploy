package com.api.tracker.controller;

import com.api.tracker.entity.ApiHistory;
import com.api.tracker.repository.ApiHistoryRepository;
import com.api.tracker.service.ApiHistoryDynamicFilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/history")
public class ApiHistoryDynamicController {

    @Autowired
    private ApiHistoryDynamicFilterService apiHistoryDynamicFilterService;

    @Autowired
    private ApiHistoryRepository apiHistoryRepository;

    // New dynamic endpoint using Map filters
    @PostMapping("/dynamic-filter")
    public List<ApiHistory> fetchApiHistoryWithFilters(@RequestBody Map<String, String> filters,
                                                       @RequestParam(name = "includePrevious", defaultValue = "false") boolean includePrevious) {
        return apiHistoryDynamicFilterService.getFilteredApiHistory(filters, includePrevious);
    }

    // Legacy endpoint using POJO-based filters
    @PostMapping("/fetchApiHistoryWithFilters")
    public List<ApiHistory> fetchApiHistoryWithFilters(@RequestBody ApiHistory filters) {
        Specification<ApiHistory> spec = ApiHistorySpecification.buildSpecification(filters);
        return apiHistoryRepository.findAll(spec);
    }
}

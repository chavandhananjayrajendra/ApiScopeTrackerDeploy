package com.api.tracker.service;

import com.api.tracker.entity.ApiHistory;
import com.api.tracker.repository.ApiHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class ApiHistoryService {

    @Autowired
    private ApiHistoryRepository apiHistoryRepository;

    public ApiHistory saveApiHistory(ApiHistory newEntry) {
        Integer maxIteration = apiHistoryRepository.findMaxIterationCount(newEntry.getMicroserviceName(), newEntry.getApiName());
        maxIteration = (maxIteration == null) ? 0 : maxIteration;

        if (newEntry.getIterationCount() > maxIteration) {
            newEntry.setModifiedAt(LocalDateTime.now());
            return apiHistoryRepository.save(newEntry);
        } else {
            throw new RuntimeException("Iteration count must be greater than the previous entry!");
        }
    }

    public List<ApiHistory> getCurrentDelivery(String microserviceName, String apiName) {
        return apiHistoryRepository.findCurrentDelivery(microserviceName, apiName);
    }

    public List<ApiHistory> getPreviousDeliveries(String microserviceName, String apiName) {
        return apiHistoryRepository.findPreviousDeliveries(microserviceName, apiName);
    }
    // ✅ New method to fetch both current and previous deliveries

    public Map<String, Object> fetchApiHistory(String microserviceName, String apiName) {
        List<ApiHistory> currentDelivery = getCurrentDelivery(microserviceName, apiName);
        List<ApiHistory> previousDeliveries = getPreviousDeliveries(microserviceName, apiName);

        Map<String, Object> response = new HashMap<>();
        if (currentDelivery.isEmpty()) {
            response.put("message", "No records found");
        } else {
            response.put("currentDelivery", currentDelivery.get(0)); // Latest entry
            response.put("previousDeliveries", previousDeliveries);
        }
        return response;
    }
}

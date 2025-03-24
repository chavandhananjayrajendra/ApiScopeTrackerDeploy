package com.api.tracker.service;

import com.api.tracker.entity.ApiHistory;
import com.api.tracker.repository.ApiHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ApiHistoryService {

    @Autowired
    private ApiHistoryRepository apiHistoryRepository;

    public ApiHistory saveApiHistory(ApiHistory newEntry) {
        Optional<Integer> maxIterationOpt = Optional.ofNullable(apiHistoryRepository.findMaxIterationCount(
                newEntry.getMicroserviceName(), newEntry.getApiName()
        ));

        if (maxIterationOpt.isPresent()) {
            int maxIteration = maxIterationOpt.get();
            if (newEntry.getIterationCount() != maxIteration + 1) {
                throw new RuntimeException("Invalid iteration count! Expected: " + (maxIteration + 1));
            }
        } else {
            if (newEntry.getIterationCount() != 1) {
                throw new RuntimeException("Invalid iteration count! Expected: 1");
            }
        }

        return apiHistoryRepository.save(newEntry);
    }

    public List<ApiHistory> getCurrentDelivery(String microserviceName, String apiName) {
        return apiHistoryRepository.findCurrentDelivery(microserviceName, apiName);
    }

    public List<ApiHistory> getPreviousDeliveries(String microserviceName, String apiName) {
        return apiHistoryRepository.findPreviousDeliveries(microserviceName, apiName);
    }

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

package com.api.tracker.service;

import com.api.tracker.entity.ApiHistory;
import com.api.tracker.repository.ApiHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiHistoryService {

    @Autowired
    private ApiHistoryRepository apiHistoryRepository;

    public List<ApiHistory> getHistoryByApi(String microserviceName, String apiName) {
        return apiHistoryRepository.findHistoryByApi(microserviceName, apiName);
    }

    public List<String> getAllMicroservices() {
        return apiHistoryRepository.findAllMicroservices();
    }

    public List<ApiHistory> getApisByMicroservice(String microserviceName) {
        return apiHistoryRepository.findByMicroserviceName(microserviceName);
    }
}

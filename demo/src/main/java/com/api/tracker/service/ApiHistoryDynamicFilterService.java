package com.api.tracker.service;

import com.api.tracker.entity.ApiHistory;
import com.api.tracker.repository.ApiHistoryRepository;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ApiHistoryDynamicFilterService {

    @Autowired
    private ApiHistoryRepository apiHistoryRepository;

    public List<ApiHistory> getFilteredApiHistory(Map<String, String> filters, boolean includePrevious) {
        Specification<ApiHistory> specification = buildSpecification(filters);
        List<ApiHistory> allMatching = apiHistoryRepository.findAll(specification);

        if (filters.isEmpty()) return allMatching;

        // Group by filtered field values except iterationCount
        Map<String, List<ApiHistory>> grouped = allMatching.stream().collect(Collectors.groupingBy(
                api -> filters.keySet().stream()
                        .filter(key -> !key.equals("iterationCount"))
                        .map(key -> key + "=" + apiFieldValue(api, key))
                        .collect(Collectors.joining("|"))
        ));

        List<ApiHistory> result = new ArrayList<>();
        for (List<ApiHistory> group : grouped.values()) {
            int maxIteration = group.stream()
                    .mapToInt(ApiHistory::getIterationCount)
                    .max().orElse(Integer.MIN_VALUE);

            for (ApiHistory entry : group) {
                if (includePrevious || entry.getIterationCount() == maxIteration) {
                    result.add(entry);
                }
            }
        }
        return result;
    }

    private Specification<ApiHistory> buildSpecification(Map<String, String> filters) {
        return (Root<ApiHistory> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            filters.forEach((key, value) -> {
                if (value != null && !value.isEmpty() && !key.equals("includePrevious")) {
                    predicates.add(cb.equal(root.get(key), value));
                }
            });
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private String apiFieldValue(ApiHistory api, String field) {
        switch (field) {
            case "microserviceName": return api.getMicroserviceName();
            case "apiName": return api.getApiName();
            case "apiStatus": return api.getApiStatus();
            case "testerName": return api.getTesterName();
            case "status": return api.getStatus();
            case "deliveryDate": return String.valueOf(api.getDeliveryDate());
            case "plannedEndDate": return String.valueOf(api.getPlannedEndDate());
            case "modifiedAt": return String.valueOf(api.getModifiedAt());

            default: return "";
        }
    }
}

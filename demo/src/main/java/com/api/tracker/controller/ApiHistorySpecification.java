package com.api.tracker.controller;


import com.api.tracker.entity.ApiHistory;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ApiHistorySpecification {

    public static Specification<ApiHistory> buildSpecification(ApiHistory filters) {
        return (Root<ApiHistory> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filters.getMicroserviceName() != null && !filters.getMicroserviceName().isEmpty()) {
                predicates.add(cb.equal(root.get("microserviceName"), filters.getMicroserviceName()));
            }
            if (filters.getApiName() != null && !filters.getApiName().isEmpty()) {
                predicates.add(cb.equal(root.get("apiName"), filters.getApiName()));
            }
            if (filters.getApiStatus() != null && !filters.getStatus().isEmpty()) {
                predicates.add(cb.equal(root.get("apiStatus"), filters.getApiStatus()));
            }
            if (filters.getTesterName() != null && !filters.getTesterName().isEmpty()) {
                predicates.add(cb.equal(root.get("testerName"), filters.getTesterName()));
            }
            if (filters.getStatus() != null && !filters.getStatus().isEmpty()) {
                predicates.add(cb.equal(root.get("status"), filters.getStatus()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

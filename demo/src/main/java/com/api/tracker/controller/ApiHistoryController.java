package com.api.tracker.controller;

import com.api.tracker.entity.ApiHistory;
import com.api.tracker.service.ApiHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api-history")
public class ApiHistoryController {

    @Autowired
    private ApiHistoryService apiHistoryService;

    @GetMapping("/{microservice}/{apiName}")
    public ResponseEntity<List<ApiHistory>> getHistory(@PathVariable String microservice, @PathVariable String apiName) {
        return ResponseEntity.ok(apiHistoryService.getHistoryByApi(microservice, apiName));
    }

    @GetMapping("/microservices")
    public ResponseEntity<List<String>> getMicroservices() {
        return ResponseEntity.ok(apiHistoryService.getAllMicroservices());
    }

    @GetMapping("/apis/{microservice}")
    public ResponseEntity<List<ApiHistory>> getApis(@PathVariable String microservice) {
        return ResponseEntity.ok(apiHistoryService.getApisByMicroservice(microservice));
    }
}

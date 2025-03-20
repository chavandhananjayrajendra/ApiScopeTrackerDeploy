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

    @PostMapping("/add")
    public ResponseEntity<?> addApiHistory(@RequestBody ApiHistory apiHistory) {
        try {
            return ResponseEntity.ok(apiHistoryService.saveApiHistory(apiHistory));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/current/{microservice}/{apiName}")
    public ResponseEntity<List<ApiHistory>> getCurrentDelivery(
            @PathVariable String microservice, @PathVariable String apiName) {
        return ResponseEntity.ok(apiHistoryService.getCurrentDelivery(microservice, apiName));
    }

    @GetMapping("/previous/{microservice}/{apiName}")
    public ResponseEntity<List<ApiHistory>> getPreviousDeliveries(
            @PathVariable String microservice, @PathVariable String apiName) {
        return ResponseEntity.ok(apiHistoryService.getPreviousDeliveries(microservice, apiName));
    }
    // ✅ New endpoint for fetchApiHistory
    @GetMapping("/fetch")
    public ResponseEntity<?> fetchApiHistory(
            @RequestParam String microserviceName, @RequestParam String apiName) {
        return ResponseEntity.ok(apiHistoryService.fetchApiHistory(microserviceName, apiName));
    }
}

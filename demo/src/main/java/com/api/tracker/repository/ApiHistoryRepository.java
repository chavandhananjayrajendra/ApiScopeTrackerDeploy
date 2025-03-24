package com.api.tracker.repository;

import com.api.tracker.entity.ApiHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApiHistoryRepository extends JpaRepository<ApiHistory, Long> {

    @Query("SELECT MAX(a.iterationCount) FROM ApiHistory a WHERE a.microserviceName = ?1 AND a.apiName = ?2")
    Integer findMaxIterationCount(String microserviceName, String apiName);

    @Query("SELECT a FROM ApiHistory a WHERE a.microserviceName = ?1 AND a.apiName = ?2 AND a.iterationCount < " +
            "(SELECT MAX(b.iterationCount) FROM ApiHistory b WHERE b.microserviceName = a.microserviceName AND b.apiName = a.apiName) " +
            "ORDER BY a.iterationCount DESC")
    List<ApiHistory> findPreviousDeliveries(String microserviceName, String apiName);

    @Query("SELECT a FROM ApiHistory a WHERE a.microserviceName = ?1 AND a.apiName = ?2 AND a.iterationCount = " +
            "(SELECT MAX(b.iterationCount) FROM ApiHistory b WHERE b.microserviceName = a.microserviceName AND b.apiName = a.apiName)")
    List<ApiHistory> findCurrentDelivery(String microserviceName, String apiName);
}

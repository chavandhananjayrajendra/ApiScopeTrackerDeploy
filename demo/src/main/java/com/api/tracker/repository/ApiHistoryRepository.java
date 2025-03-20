package com.api.tracker.repository;

import com.api.tracker.entity.ApiHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ApiHistoryRepository extends JpaRepository<ApiHistory, Long> {
    @Query("SELECT a FROM ApiHistory a WHERE a.microserviceName = :microserviceName AND a.apiName = :apiName ORDER BY a.modifiedAt DESC")
    List<ApiHistory> findHistoryByApi(@Param("microserviceName") String microserviceName, @Param("apiName") String apiName);

    @Query("SELECT DISTINCT a.microserviceName FROM ApiHistory a")
    List<String> findAllMicroservices();

    @Query("SELECT DISTINCT a.apiName FROM ApiHistory a WHERE a.microserviceName = :microserviceName")
    List<String> findApisByMicroservice(@Param("microserviceName") String microserviceName);

    @Query("SELECT MAX(a.iterationCount) FROM ApiHistory a WHERE a.microserviceName = :microserviceName AND a.apiName = :apiName")
    Integer getMaxIterationCount(@Param("microserviceName") String microserviceName, @Param("apiName") String apiName);

    @Query("SELECT a FROM ApiHistory a WHERE a.microserviceName = :microserviceName")
    List<ApiHistory> findByMicroserviceName(String microserviceName); // ✅ Ensure this method exists

}


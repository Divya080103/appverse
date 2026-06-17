package com.appverse.repository;

import com.appverse.entity.App;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppRepository extends JpaRepository<App, Long> {
    List<App> findByActiveTrue();
    List<App> findByCategoryId(Long categoryId);
    List<App> findByDeveloperId(Long developerId);
    List<App> findByNameContainingIgnoreCase(String name);

    @Query("SELECT a FROM App a WHERE a.active = true ORDER BY a.downloadCount DESC")
    List<App> findTrendingApps();

    @Query("SELECT a FROM App a WHERE a.active = true AND (LOWER(a.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(a.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<App> searchApps(@Param("keyword") String keyword);

    @Query("SELECT a FROM App a WHERE a.active = true ORDER BY a.averageRating DESC")
    List<App> findTopRatedApps();

    Long countByDeveloperId(Long developerId);
}

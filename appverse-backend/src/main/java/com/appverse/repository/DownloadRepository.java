package com.appverse.repository;

import com.appverse.entity.Download;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DownloadRepository extends JpaRepository<Download, Long> {
    List<Download> findByUserId(Long userId);
    List<Download> findByAppId(Long appId);
    Long countByAppId(Long appId);

    @Query("SELECT d.app.id, COUNT(d) FROM Download d WHERE d.app.developer.id = :developerId GROUP BY d.app.id")
    List<Object[]> getDownloadCountsByDeveloper(@Param("developerId") Long developerId);

    @Query("SELECT FUNCTION('MONTH', d.downloadedAt), COUNT(d) FROM Download d WHERE d.app.developer.id = :developerId GROUP BY FUNCTION('MONTH', d.downloadedAt)")
    List<Object[]> getMonthlyDownloadsByDeveloper(@Param("developerId") Long developerId);
}

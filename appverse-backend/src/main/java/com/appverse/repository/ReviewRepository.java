package com.appverse.repository;

import com.appverse.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByAppId(Long appId);
    List<Review> findByUserId(Long userId);
    boolean existsByUserIdAndAppId(Long userId, Long appId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.app.id = :appId")
    Double getAverageRatingByAppId(@Param("appId") Long appId);

    Long countByAppId(Long appId);
}

package com.appverse.service;

import com.appverse.dto.ReviewDTO;

import java.util.List;

public interface ReviewService {
    ReviewDTO createReview(ReviewDTO reviewDTO, Long userId);
    ReviewDTO updateReview(Long id, ReviewDTO reviewDTO, Long userId);
    void deleteReview(Long id, Long userId);
    List<ReviewDTO> getReviewsByApp(Long appId);
    List<ReviewDTO> getReviewsByUser(Long userId);
    ReviewDTO getReviewById(Long id);
}

package com.appverse.service.impl;

import com.appverse.dto.ReviewDTO;
import com.appverse.entity.App;
import com.appverse.entity.Review;
import com.appverse.entity.User;
import com.appverse.exception.BadRequestException;
import com.appverse.exception.ResourceNotFoundException;
import com.appverse.exception.UnauthorizedException;
import com.appverse.repository.AppRepository;
import com.appverse.repository.ReviewRepository;
import com.appverse.repository.UserRepository;
import com.appverse.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final AppRepository appRepository;

    @Override
    @Transactional
    public ReviewDTO createReview(ReviewDTO reviewDTO, Long userId) {
        if (reviewRepository.existsByUserIdAndAppId(userId, reviewDTO.getAppId())) {
            throw new BadRequestException("You have already reviewed this app");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        App app = appRepository.findById(reviewDTO.getAppId())
                .orElseThrow(() -> new ResourceNotFoundException("App not found"));

        Review review = Review.builder()
                .rating(reviewDTO.getRating())
                .comment(reviewDTO.getComment())
                .user(user)
                .app(app)
                .build();

        Review savedReview = reviewRepository.save(review);

        // Update app average rating
        Double avgRating = reviewRepository.getAverageRatingByAppId(app.getId());
        app.setAverageRating(avgRating != null ? avgRating : 0.0);
        appRepository.save(app);

        return mapToDTO(savedReview);
    }

    @Override
    @Transactional
    public ReviewDTO updateReview(Long id, ReviewDTO reviewDTO, Long userId) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        if (!review.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("You can only update your own reviews");
        }

        if (reviewDTO.getRating() != null) review.setRating(reviewDTO.getRating());
        if (reviewDTO.getComment() != null) review.setComment(reviewDTO.getComment());

        Review updatedReview = reviewRepository.save(review);

        // Update app average rating
        Double avgRating = reviewRepository.getAverageRatingByAppId(review.getApp().getId());
        App app = review.getApp();
        app.setAverageRating(avgRating != null ? avgRating : 0.0);
        appRepository.save(app);

        return mapToDTO(updatedReview);
    }

    @Override
    @Transactional
    public void deleteReview(Long id, Long userId) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        if (!review.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("You can only delete your own reviews");
        }

        Long appId = review.getApp().getId();
        reviewRepository.delete(review);

        // Update app average rating
        Double avgRating = reviewRepository.getAverageRatingByAppId(appId);
        App app = appRepository.findById(appId).orElse(null);
        if (app != null) {
            app.setAverageRating(avgRating != null ? avgRating : 0.0);
            appRepository.save(app);
        }
    }

    @Override
    public List<ReviewDTO> getReviewsByApp(Long appId) {
        return reviewRepository.findByAppId(appId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReviewDTO> getReviewsByUser(Long userId) {
        return reviewRepository.findByUserId(userId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ReviewDTO getReviewById(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));
        return mapToDTO(review);
    }

    private ReviewDTO mapToDTO(Review review) {
        return ReviewDTO.builder()
                .id(review.getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .userId(review.getUser().getId())
                .username(review.getUser().getUsername())
                .appId(review.getApp().getId())
                .appName(review.getApp().getName())
                .createdAt(review.getCreatedAt())
                .build();
    }
}

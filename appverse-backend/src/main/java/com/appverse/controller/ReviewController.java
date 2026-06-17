package com.appverse.controller;

import com.appverse.dto.ReviewDTO;
import com.appverse.entity.User;
import com.appverse.service.ReviewService;
import com.appverse.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;

    @GetMapping("/app/{appId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByApp(@PathVariable Long appId) {
        return ResponseEntity.ok(reviewService.getReviewsByApp(appId));
    }

    @GetMapping("/user")
    public ResponseEntity<List<ReviewDTO>> getMyReviews(Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        return ResponseEntity.ok(reviewService.getReviewsByUser(user.getId()));
    }

    @PostMapping
    public ResponseEntity<ReviewDTO> createReview(@Valid @RequestBody ReviewDTO reviewDTO, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        ReviewDTO created = reviewService.createReview(reviewDTO, user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewDTO> updateReview(@PathVariable Long id, @Valid @RequestBody ReviewDTO reviewDTO, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        ReviewDTO updated = reviewService.updateReview(id, reviewDTO, user.getId());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        reviewService.deleteReview(id, user.getId());
        return ResponseEntity.noContent().build();
    }
}

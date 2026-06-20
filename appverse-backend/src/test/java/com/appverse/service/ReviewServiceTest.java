package com.appverse.service;

import com.appverse.dto.ReviewDTO;
import com.appverse.entity.App;
import com.appverse.entity.Category;
import com.appverse.entity.Review;
import com.appverse.entity.User;
import com.appverse.enums.Role;
import com.appverse.exception.BadRequestException;
import com.appverse.exception.ResourceNotFoundException;
import com.appverse.exception.UnauthorizedException;
import com.appverse.repository.AppRepository;
import com.appverse.repository.ReviewRepository;
import com.appverse.repository.UserRepository;
import com.appverse.service.impl.ReviewServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AppRepository appRepository;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    private User testUser;
    private App testApp;
    private Review testReview;
    private ReviewDTO reviewDTO;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .fullName("Test User")
                .role(Role.USER)
                .build();

        Category category = Category.builder()
                .id(1L)
                .name("Productivity")
                .build();

        User developer = User.builder()
                .id(2L)
                .username("developer")
                .fullName("Developer")
                .role(Role.DEVELOPER)
                .build();

        testApp = App.builder()
                .id(1L)
                .name("TestApp")
                .description("A test app")
                .developer(developer)
                .category(category)
                .downloadCount(100L)
                .averageRating(4.0)
                .active(true)
                .build();

        testReview = Review.builder()
                .id(1L)
                .rating(5)
                .comment("Great app!")
                .user(testUser)
                .app(testApp)
                .createdAt(LocalDateTime.now())
                .build();

        reviewDTO = ReviewDTO.builder()
                .appId(1L)
                .rating(5)
                .comment("Great app!")
                .build();
    }

    @Test
    void testCreateReview_Success() {
        when(reviewRepository.existsByUserIdAndAppId(1L, 1L)).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(appRepository.findById(1L)).thenReturn(Optional.of(testApp));
        when(reviewRepository.save(any(Review.class))).thenReturn(testReview);
        when(reviewRepository.getAverageRatingByAppId(1L)).thenReturn(4.5);
        when(appRepository.save(any(App.class))).thenReturn(testApp);

        ReviewDTO result = reviewService.createReview(reviewDTO, 1L);

        assertNotNull(result);
        assertEquals(5, result.getRating());
        assertEquals("Great app!", result.getComment());
        verify(reviewRepository).save(any(Review.class));
    }

    @Test
    void testCreateReview_AlreadyReviewed() {
        when(reviewRepository.existsByUserIdAndAppId(1L, 1L)).thenReturn(true);

        assertThrows(BadRequestException.class, () -> reviewService.createReview(reviewDTO, 1L));
        verify(reviewRepository, never()).save(any(Review.class));
    }

    @Test
    void testCreateReview_UserNotFound() {
        when(reviewRepository.existsByUserIdAndAppId(99L, 1L)).thenReturn(false);
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reviewService.createReview(reviewDTO, 99L));
    }

    @Test
    void testCreateReview_AppNotFound() {
        when(reviewRepository.existsByUserIdAndAppId(1L, 1L)).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(appRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reviewService.createReview(reviewDTO, 1L));
    }

    @Test
    void testUpdateReview_Success() {
        ReviewDTO updateDTO = ReviewDTO.builder().rating(4).comment("Updated comment").build();
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(testReview));
        when(reviewRepository.save(any(Review.class))).thenReturn(testReview);
        when(reviewRepository.getAverageRatingByAppId(1L)).thenReturn(4.0);
        when(appRepository.save(any(App.class))).thenReturn(testApp);

        ReviewDTO result = reviewService.updateReview(1L, updateDTO, 1L);

        assertNotNull(result);
        verify(reviewRepository).save(any(Review.class));
    }

    @Test
    void testUpdateReview_NotFound() {
        when(reviewRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reviewService.updateReview(99L, reviewDTO, 1L));
    }

    @Test
    void testUpdateReview_Unauthorized() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(testReview));

        assertThrows(UnauthorizedException.class, () -> reviewService.updateReview(1L, reviewDTO, 99L));
    }

    @Test
    void testDeleteReview_Success() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(testReview));
        when(reviewRepository.getAverageRatingByAppId(1L)).thenReturn(null);
        when(appRepository.findById(1L)).thenReturn(Optional.of(testApp));
        when(appRepository.save(any(App.class))).thenReturn(testApp);

        reviewService.deleteReview(1L, 1L);

        verify(reviewRepository).delete(testReview);
    }

    @Test
    void testDeleteReview_NotFound() {
        when(reviewRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reviewService.deleteReview(99L, 1L));
    }

    @Test
    void testDeleteReview_Unauthorized() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(testReview));

        assertThrows(UnauthorizedException.class, () -> reviewService.deleteReview(1L, 99L));
    }

    @Test
    void testGetReviewsByApp() {
        when(reviewRepository.findByAppId(1L)).thenReturn(Arrays.asList(testReview));

        List<ReviewDTO> result = reviewService.getReviewsByApp(1L);

        assertEquals(1, result.size());
        assertEquals(5, result.get(0).getRating());
    }

    @Test
    void testGetReviewsByUser() {
        when(reviewRepository.findByUserId(1L)).thenReturn(Arrays.asList(testReview));

        List<ReviewDTO> result = reviewService.getReviewsByUser(1L);

        assertEquals(1, result.size());
        assertEquals("Great app!", result.get(0).getComment());
    }

    @Test
    void testGetReviewsByApp_Empty() {
        when(reviewRepository.findByAppId(99L)).thenReturn(Collections.emptyList());

        List<ReviewDTO> result = reviewService.getReviewsByApp(99L);

        assertTrue(result.isEmpty());
    }
}

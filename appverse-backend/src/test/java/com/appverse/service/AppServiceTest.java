package com.appverse.service;

import com.appverse.dto.AppDTO;
import com.appverse.entity.App;
import com.appverse.entity.Category;
import com.appverse.entity.User;
import com.appverse.enums.Role;
import com.appverse.exception.BadRequestException;
import com.appverse.exception.ResourceNotFoundException;
import com.appverse.exception.UnauthorizedException;
import com.appverse.repository.AppRepository;
import com.appverse.repository.CategoryRepository;
import com.appverse.repository.UserRepository;
import com.appverse.service.impl.AppServiceImpl;
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
public class AppServiceTest {

    @Mock
    private AppRepository appRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private AppServiceImpl appService;

    private User developer;
    private Category category;
    private App testApp;
    private AppDTO appDTO;

    @BeforeEach
    void setUp() {
        developer = User.builder()
                .id(1L)
                .username("developer1")
                .email("dev@example.com")
                .fullName("Developer One")
                .role(Role.DEVELOPER)
                .build();

        category = Category.builder()
                .id(1L)
                .name("Productivity")
                .description("Productivity apps")
                .build();

        testApp = App.builder()
                .id(1L)
                .name("TestApp")
                .description("A test application")
                .currentVersion("1.0.0")
                .iconUrl("http://icon.url")
                .screenshotUrl("http://screenshot.url")
                .developer(developer)
                .category(category)
                .downloadCount(100L)
                .averageRating(4.5)
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        appDTO = AppDTO.builder()
                .name("TestApp")
                .description("A test application")
                .currentVersion("1.0.0")
                .iconUrl("http://icon.url")
                .screenshotUrl("http://screenshot.url")
                .categoryId(1L)
                .build();
    }

    @Test
    void testCreateApp_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(developer));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(appRepository.save(any(App.class))).thenReturn(testApp);

        AppDTO result = appService.createApp(appDTO, 1L);

        assertNotNull(result);
        assertEquals("TestApp", result.getName());
        assertEquals("Developer One", result.getDeveloperName());
        verify(appRepository).save(any(App.class));
    }

    @Test
    void testCreateApp_DeveloperNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> appService.createApp(appDTO, 99L));
    }

    @Test
    void testCreateApp_CategoryNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(developer));
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> appService.createApp(appDTO, 1L));
    }

    @Test
    void testUpdateApp_Success() {
        AppDTO updateDTO = AppDTO.builder().name("UpdatedApp").categoryId(null).build();
        when(appRepository.findById(1L)).thenReturn(Optional.of(testApp));
        when(appRepository.save(any(App.class))).thenReturn(testApp);

        AppDTO result = appService.updateApp(1L, updateDTO, 1L);

        assertNotNull(result);
        verify(appRepository).save(any(App.class));
    }

    @Test
    void testUpdateApp_Unauthorized() {
        when(appRepository.findById(1L)).thenReturn(Optional.of(testApp));

        assertThrows(UnauthorizedException.class, () -> appService.updateApp(1L, appDTO, 99L));
    }

    @Test
    void testGetAppById_Success() {
        when(appRepository.findById(1L)).thenReturn(Optional.of(testApp));

        AppDTO result = appService.getAppById(1L);

        assertNotNull(result);
        assertEquals("TestApp", result.getName());
    }

    @Test
    void testGetAppById_NotFound() {
        when(appRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> appService.getAppById(99L));
    }

    @Test
    void testGetAllApps() {
        when(appRepository.findByActiveTrue()).thenReturn(Arrays.asList(testApp));

        List<AppDTO> result = appService.getAllApps();

        assertEquals(1, result.size());
        assertEquals("TestApp", result.get(0).getName());
    }

    @Test
    void testGetAppsByCategory() {
        when(appRepository.findByCategoryId(1L)).thenReturn(Arrays.asList(testApp));

        List<AppDTO> result = appService.getAppsByCategory(1L);

        assertEquals(1, result.size());
    }

    @Test
    void testGetAppsByDeveloper() {
        when(appRepository.findByDeveloperId(1L)).thenReturn(Arrays.asList(testApp));

        List<AppDTO> result = appService.getAppsByDeveloper(1L);

        assertEquals(1, result.size());
    }

    @Test
    void testSearchApps_Success() {
        when(appRepository.searchApps("Test")).thenReturn(Arrays.asList(testApp));

        List<AppDTO> result = appService.searchApps("Test");

        assertEquals(1, result.size());
    }

    @Test
    void testSearchApps_EmptyKeyword() {
        assertThrows(BadRequestException.class, () -> appService.searchApps(""));
    }

    @Test
    void testSearchApps_NullKeyword() {
        assertThrows(BadRequestException.class, () -> appService.searchApps(null));
    }

    @Test
    void testGetTrendingApps() {
        when(appRepository.findTrendingApps()).thenReturn(Arrays.asList(testApp));

        List<AppDTO> result = appService.getTrendingApps();

        assertEquals(1, result.size());
    }

    @Test
    void testGetTopRatedApps() {
        when(appRepository.findTopRatedApps()).thenReturn(Arrays.asList(testApp));

        List<AppDTO> result = appService.getTopRatedApps();

        assertEquals(1, result.size());
    }

    @Test
    void testDeleteApp_Success() {
        when(appRepository.findById(1L)).thenReturn(Optional.of(testApp));

        appService.deleteApp(1L, 1L);

        verify(appRepository).delete(testApp);
    }

    @Test
    void testDeleteApp_Unauthorized() {
        when(appRepository.findById(1L)).thenReturn(Optional.of(testApp));

        assertThrows(UnauthorizedException.class, () -> appService.deleteApp(1L, 99L));
    }

    @Test
    void testDeactivateApp_Success() {
        when(appRepository.findById(1L)).thenReturn(Optional.of(testApp));
        when(appRepository.save(any(App.class))).thenReturn(testApp);

        appService.deactivateApp(1L);

        assertFalse(testApp.isActive());
        verify(appRepository).save(testApp);
    }
}

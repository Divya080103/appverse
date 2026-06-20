package com.appverse.service;

import com.appverse.dto.AnalyticsDTO;
import com.appverse.dto.AppVersionDTO;
import com.appverse.entity.App;
import com.appverse.entity.AppVersion;
import com.appverse.entity.Download;
import com.appverse.entity.User;
import com.appverse.enums.Role;
import com.appverse.exception.ResourceNotFoundException;
import com.appverse.exception.UnauthorizedException;
import com.appverse.repository.*;
import com.appverse.service.impl.DeveloperServiceImpl;
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
public class DeveloperServiceTest {

    @Mock
    private AppRepository appRepository;

    @Mock
    private AppVersionRepository appVersionRepository;

    @Mock
    private DownloadRepository downloadRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private DeveloperServiceImpl developerService;

    private User developer;
    private App testApp;
    private AppVersion testVersion;
    private AppVersionDTO versionDTO;

    @BeforeEach
    void setUp() {
        developer = User.builder()
                .id(1L)
                .username("developer1")
                .email("dev@example.com")
                .fullName("Developer One")
                .role(Role.DEVELOPER)
                .build();

        testApp = App.builder()
                .id(1L)
                .name("TestApp")
                .description("A test app")
                .currentVersion("1.0.0")
                .developer(developer)
                .downloadCount(50L)
                .averageRating(4.0)
                .active(true)
                .build();

        testVersion = AppVersion.builder()
                .id(1L)
                .versionNumber("2.0.0")
                .releaseNotes("New features")
                .downloadUrl("http://download.url")
                .fileSize(1024L)
                .app(testApp)
                .releasedAt(LocalDateTime.now())
                .build();

        versionDTO = AppVersionDTO.builder()
                .versionNumber("2.0.0")
                .releaseNotes("New features")
                .downloadUrl("http://download.url")
                .fileSize(1024L)
                .build();
    }

    @Test
    void testAddVersion_Success() {
        when(appRepository.findById(1L)).thenReturn(Optional.of(testApp));
        when(appVersionRepository.save(any(AppVersion.class))).thenReturn(testVersion);
        when(appRepository.save(any(App.class))).thenReturn(testApp);

        AppVersionDTO result = developerService.addVersion(1L, versionDTO, 1L);

        assertNotNull(result);
        assertEquals("2.0.0", result.getVersionNumber());
        assertEquals("New features", result.getReleaseNotes());
        verify(appVersionRepository).save(any(AppVersion.class));
        verify(appRepository).save(any(App.class));
    }

    @Test
    void testAddVersion_AppNotFound() {
        when(appRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> developerService.addVersion(99L, versionDTO, 1L));
    }

    @Test
    void testAddVersion_Unauthorized() {
        when(appRepository.findById(1L)).thenReturn(Optional.of(testApp));

        assertThrows(UnauthorizedException.class, () -> developerService.addVersion(1L, versionDTO, 99L));
    }

    @Test
    void testGetVersionsByApp() {
        when(appVersionRepository.findByAppIdOrderByReleasedAtDesc(1L))
                .thenReturn(Arrays.asList(testVersion));

        List<AppVersionDTO> result = developerService.getVersionsByApp(1L);

        assertEquals(1, result.size());
        assertEquals("2.0.0", result.get(0).getVersionNumber());
    }

    @Test
    void testGetDeveloperAnalytics_Success() {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(appRepository.findByDeveloperId(1L)).thenReturn(Arrays.asList(testApp));
        when(reviewRepository.countByAppId(1L)).thenReturn(10L);

        AnalyticsDTO result = developerService.getDeveloperAnalytics(1L);

        assertNotNull(result);
        assertEquals(50L, result.getTotalDownloads());
        assertEquals(1L, result.getTotalApps());
        assertEquals(4.0, result.getAverageRating());
        assertEquals(10L, result.getTotalReviews());
    }

    @Test
    void testGetDeveloperAnalytics_DeveloperNotFound() {
        when(userRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> developerService.getDeveloperAnalytics(99L));
    }

    @Test
    void testGetDeveloperAnalytics_NoApps() {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(appRepository.findByDeveloperId(1L)).thenReturn(Collections.emptyList());

        AnalyticsDTO result = developerService.getDeveloperAnalytics(1L);

        assertEquals(0L, result.getTotalDownloads());
        assertEquals(0L, result.getTotalApps());
        assertEquals(0.0, result.getAverageRating());
    }

    @Test
    void testRecordDownload_Success() {
        when(appRepository.findById(1L)).thenReturn(Optional.of(testApp));
        when(userRepository.findById(1L)).thenReturn(Optional.of(developer));
        when(downloadRepository.save(any(Download.class))).thenReturn(new Download());
        when(appRepository.save(any(App.class))).thenReturn(testApp);

        developerService.recordDownload(1L, 1L);

        assertEquals(51L, testApp.getDownloadCount());
        verify(downloadRepository).save(any(Download.class));
        verify(appRepository).save(testApp);
    }

    @Test
    void testRecordDownload_AppNotFound() {
        when(appRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> developerService.recordDownload(99L, 1L));
    }

    @Test
    void testRecordDownload_UserNotFound() {
        when(appRepository.findById(1L)).thenReturn(Optional.of(testApp));
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> developerService.recordDownload(1L, 99L));
    }
}

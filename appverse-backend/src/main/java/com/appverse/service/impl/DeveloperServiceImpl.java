package com.appverse.service.impl;

import com.appverse.dto.AnalyticsDTO;
import com.appverse.dto.AppVersionDTO;
import com.appverse.entity.App;
import com.appverse.entity.AppVersion;
import com.appverse.entity.Download;
import com.appverse.entity.User;
import com.appverse.exception.ResourceNotFoundException;
import com.appverse.exception.UnauthorizedException;
import com.appverse.repository.*;
import com.appverse.service.DeveloperService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeveloperServiceImpl implements DeveloperService {

    private final AppRepository appRepository;
    private final AppVersionRepository appVersionRepository;
    private final DownloadRepository downloadRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    @Override
    @Transactional
    public AppVersionDTO addVersion(Long appId, AppVersionDTO versionDTO, Long developerId) {
        App app = appRepository.findById(appId)
                .orElseThrow(() -> new ResourceNotFoundException("App not found"));

        if (!app.getDeveloper().getId().equals(developerId)) {
            throw new UnauthorizedException("You can only add versions to your own apps");
        }

        AppVersion version = AppVersion.builder()
                .versionNumber(versionDTO.getVersionNumber())
                .releaseNotes(versionDTO.getReleaseNotes())
                .downloadUrl(versionDTO.getDownloadUrl())
                .fileSize(versionDTO.getFileSize())
                .app(app)
                .build();

        AppVersion savedVersion = appVersionRepository.save(version);

        // Update app current version
        app.setCurrentVersion(versionDTO.getVersionNumber());
        appRepository.save(app);

        return mapVersionToDTO(savedVersion);
    }

    @Override
    public List<AppVersionDTO> getVersionsByApp(Long appId) {
        return appVersionRepository.findByAppIdOrderByReleasedAtDesc(appId).stream()
                .map(this::mapVersionToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AnalyticsDTO getDeveloperAnalytics(Long developerId) {
        if (!userRepository.existsById(developerId)) {
            throw new ResourceNotFoundException("Developer not found");
        }

        List<App> developerApps = appRepository.findByDeveloperId(developerId);

        long totalDownloads = developerApps.stream()
                .mapToLong(app -> app.getDownloadCount() != null ? app.getDownloadCount() : 0)
                .sum();

        double avgRating = developerApps.stream()
                .filter(app -> app.getAverageRating() != null && app.getAverageRating() > 0)
                .mapToDouble(App::getAverageRating)
                .average()
                .orElse(0.0);

        long totalReviews = developerApps.stream()
                .mapToLong(app -> reviewRepository.countByAppId(app.getId()))
                .sum();

        Map<String, Long> downloadsByApp = new HashMap<>();
        for (App app : developerApps) {
            downloadsByApp.put(app.getName(), app.getDownloadCount() != null ? app.getDownloadCount() : 0);
        }

        return AnalyticsDTO.builder()
                .totalDownloads(totalDownloads)
                .totalApps((long) developerApps.size())
                .averageRating(avgRating)
                .totalReviews(totalReviews)
                .downloadsByApp(downloadsByApp)
                .build();
    }

    @Override
    @Transactional
    public void recordDownload(Long appId, Long userId) {
        App app = appRepository.findById(appId)
                .orElseThrow(() -> new ResourceNotFoundException("App not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Download download = Download.builder()
                .app(app)
                .user(user)
                .version(app.getCurrentVersion())
                .build();

        downloadRepository.save(download);

        // Increment download count
        app.setDownloadCount(app.getDownloadCount() + 1);
        appRepository.save(app);
    }

    private AppVersionDTO mapVersionToDTO(AppVersion version) {
        return AppVersionDTO.builder()
                .id(version.getId())
                .versionNumber(version.getVersionNumber())
                .releaseNotes(version.getReleaseNotes())
                .downloadUrl(version.getDownloadUrl())
                .fileSize(version.getFileSize())
                .appId(version.getApp().getId())
                .releasedAt(version.getReleasedAt())
                .build();
    }
}

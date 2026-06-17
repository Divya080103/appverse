package com.appverse.service.impl;

import com.appverse.dto.AppDTO;
import com.appverse.entity.App;
import com.appverse.entity.Category;
import com.appverse.entity.User;
import com.appverse.exception.BadRequestException;
import com.appverse.exception.ResourceNotFoundException;
import com.appverse.exception.UnauthorizedException;
import com.appverse.repository.AppRepository;
import com.appverse.repository.CategoryRepository;
import com.appverse.repository.UserRepository;
import com.appverse.service.AppService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppServiceImpl implements AppService {

    private final AppRepository appRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public AppDTO createApp(AppDTO appDTO, Long developerId) {
        User developer = userRepository.findById(developerId)
                .orElseThrow(() -> new ResourceNotFoundException("Developer not found"));

        Category category = categoryRepository.findById(appDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        App app = App.builder()
                .name(appDTO.getName())
                .description(appDTO.getDescription())
                .currentVersion(appDTO.getCurrentVersion())
                .iconUrl(appDTO.getIconUrl())
                .screenshotUrl(appDTO.getScreenshotUrl())
                .developer(developer)
                .category(category)
                .build();

        App savedApp = appRepository.save(app);
        return mapToDTO(savedApp);
    }

    @Override
    public AppDTO updateApp(Long id, AppDTO appDTO, Long developerId) {
        App app = appRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("App not found with id: " + id));

        if (!app.getDeveloper().getId().equals(developerId)) {
            throw new UnauthorizedException("You can only update your own apps");
        }

        if (appDTO.getName() != null) app.setName(appDTO.getName());
        if (appDTO.getDescription() != null) app.setDescription(appDTO.getDescription());
        if (appDTO.getCurrentVersion() != null) app.setCurrentVersion(appDTO.getCurrentVersion());
        if (appDTO.getIconUrl() != null) app.setIconUrl(appDTO.getIconUrl());
        if (appDTO.getScreenshotUrl() != null) app.setScreenshotUrl(appDTO.getScreenshotUrl());

        if (appDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(appDTO.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            app.setCategory(category);
        }

        App updatedApp = appRepository.save(app);
        return mapToDTO(updatedApp);
    }

    @Override
    public AppDTO getAppById(Long id) {
        App app = appRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("App not found with id: " + id));
        return mapToDTO(app);
    }

    @Override
    public List<AppDTO> getAllApps() {
        return appRepository.findByActiveTrue().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppDTO> getAppsByCategory(Long categoryId) {
        return appRepository.findByCategoryId(categoryId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppDTO> getAppsByDeveloper(Long developerId) {
        return appRepository.findByDeveloperId(developerId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppDTO> searchApps(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new BadRequestException("Search keyword cannot be empty");
        }
        return appRepository.searchApps(keyword).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppDTO> getTrendingApps() {
        return appRepository.findTrendingApps().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppDTO> getTopRatedApps() {
        return appRepository.findTopRatedApps().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteApp(Long id, Long developerId) {
        App app = appRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("App not found with id: " + id));

        if (!app.getDeveloper().getId().equals(developerId)) {
            throw new UnauthorizedException("You can only delete your own apps");
        }

        appRepository.delete(app);
    }

    @Override
    public void deactivateApp(Long id) {
        App app = appRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("App not found with id: " + id));
        app.setActive(false);
        appRepository.save(app);
    }

    private AppDTO mapToDTO(App app) {
        return AppDTO.builder()
                .id(app.getId())
                .name(app.getName())
                .description(app.getDescription())
                .currentVersion(app.getCurrentVersion())
                .iconUrl(app.getIconUrl())
                .screenshotUrl(app.getScreenshotUrl())
                .downloadCount(app.getDownloadCount())
                .averageRating(app.getAverageRating())
                .developerId(app.getDeveloper().getId())
                .developerName(app.getDeveloper().getFullName())
                .categoryId(app.getCategory().getId())
                .categoryName(app.getCategory().getName())
                .active(app.isActive())
                .createdAt(app.getCreatedAt() != null ? app.getCreatedAt().toString() : null)
                .build();
    }
}

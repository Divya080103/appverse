package com.appverse.service;

import com.appverse.dto.AppDTO;

import java.util.List;

public interface AppService {
    AppDTO createApp(AppDTO appDTO, Long developerId);
    AppDTO updateApp(Long id, AppDTO appDTO, Long developerId);
    AppDTO getAppById(Long id);
    List<AppDTO> getAllApps();
    List<AppDTO> getAppsByCategory(Long categoryId);
    List<AppDTO> getAppsByDeveloper(Long developerId);
    List<AppDTO> searchApps(String keyword);
    List<AppDTO> getTrendingApps();
    List<AppDTO> getTopRatedApps();
    void deleteApp(Long id, Long developerId);
    void deactivateApp(Long id);
}

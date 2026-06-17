package com.appverse.service;

import com.appverse.dto.AnalyticsDTO;
import com.appverse.dto.AppVersionDTO;

import java.util.List;

public interface DeveloperService {
    AppVersionDTO addVersion(Long appId, AppVersionDTO versionDTO, Long developerId);
    List<AppVersionDTO> getVersionsByApp(Long appId);
    AnalyticsDTO getDeveloperAnalytics(Long developerId);
    void recordDownload(Long appId, Long userId);
}

package com.appverse.controller;

import com.appverse.dto.AnalyticsDTO;
import com.appverse.dto.AppDTO;
import com.appverse.dto.AppVersionDTO;
import com.appverse.entity.User;
import com.appverse.service.AppService;
import com.appverse.service.DeveloperService;
import com.appverse.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/developer")
@RequiredArgsConstructor
public class DeveloperController {

    private final DeveloperService developerService;
    private final AppService appService;
    private final UserService userService;

    @GetMapping("/apps")
    public ResponseEntity<List<AppDTO>> getMyApps(Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        return ResponseEntity.ok(appService.getAppsByDeveloper(user.getId()));
    }

    @PostMapping("/apps")
    public ResponseEntity<AppDTO> publishApp(@Valid @RequestBody AppDTO appDTO, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        AppDTO created = appService.createApp(appDTO, user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/apps/{id}")
    public ResponseEntity<AppDTO> updateApp(@PathVariable Long id, @Valid @RequestBody AppDTO appDTO, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        AppDTO updated = appService.updateApp(id, appDTO, user.getId());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/apps/{id}")
    public ResponseEntity<Void> deleteApp(@PathVariable Long id, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        appService.deleteApp(id, user.getId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/apps/{appId}/versions")
    public ResponseEntity<AppVersionDTO> addVersion(@PathVariable Long appId, @Valid @RequestBody AppVersionDTO versionDTO, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        AppVersionDTO created = developerService.addVersion(appId, versionDTO, user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/apps/{appId}/versions")
    public ResponseEntity<List<AppVersionDTO>> getVersions(@PathVariable Long appId) {
        return ResponseEntity.ok(developerService.getVersionsByApp(appId));
    }

    @GetMapping("/analytics")
    public ResponseEntity<AnalyticsDTO> getAnalytics(Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        return ResponseEntity.ok(developerService.getDeveloperAnalytics(user.getId()));
    }
}

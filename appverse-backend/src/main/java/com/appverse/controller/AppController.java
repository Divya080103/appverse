package com.appverse.controller;

import com.appverse.dto.AppDTO;
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
@RequestMapping("/api/apps")
@RequiredArgsConstructor
public class AppController {

    private final AppService appService;
    private final DeveloperService developerService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<AppDTO>> getAllApps() {
        return ResponseEntity.ok(appService.getAllApps());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppDTO> getAppById(@PathVariable Long id) {
        return ResponseEntity.ok(appService.getAppById(id));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<AppDTO>> getAppsByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(appService.getAppsByCategory(categoryId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<AppDTO>> searchApps(@RequestParam String keyword) {
        return ResponseEntity.ok(appService.searchApps(keyword));
    }

    @GetMapping("/trending")
    public ResponseEntity<List<AppDTO>> getTrendingApps() {
        return ResponseEntity.ok(appService.getTrendingApps());
    }

    @GetMapping("/top-rated")
    public ResponseEntity<List<AppDTO>> getTopRatedApps() {
        return ResponseEntity.ok(appService.getTopRatedApps());
    }

    @PostMapping("/{id}/download")
    public ResponseEntity<Void> downloadApp(@PathVariable Long id, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        developerService.recordDownload(id, user.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<AppDTO> createApp(@Valid @RequestBody AppDTO appDTO, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        AppDTO created = appService.createApp(appDTO, user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppDTO> updateApp(@PathVariable Long id, @Valid @RequestBody AppDTO appDTO, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        AppDTO updated = appService.updateApp(id, appDTO, user.getId());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApp(@PathVariable Long id, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        appService.deleteApp(id, user.getId());
        return ResponseEntity.noContent().build();
    }
}

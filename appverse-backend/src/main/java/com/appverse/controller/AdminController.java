package com.appverse.controller;

import com.appverse.dto.AppDTO;
import com.appverse.dto.CategoryDTO;
import com.appverse.dto.UserDTO;
import com.appverse.service.AppService;
import com.appverse.service.CategoryService;
import com.appverse.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {

    private final UserService userService;
    private final AppService appService;
    private final CategoryService categoryService;

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/users/role/{role}")
    public ResponseEntity<List<UserDTO>> getUsersByRole(@PathVariable String role) {
        return ResponseEntity.ok(userService.getUsersByRole(role));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/apps")
    public ResponseEntity<List<AppDTO>> getAllApps() {
        return ResponseEntity.ok(appService.getAllApps());
    }

    @PutMapping("/apps/{id}/deactivate")
    public ResponseEntity<Void> deactivateApp(@PathVariable Long id) {
        appService.deactivateApp(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboard() {
        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("totalUsers", userService.getAllUsers().size());
        dashboard.put("totalApps", appService.getAllApps().size());
        dashboard.put("totalCategories", categoryService.getAllCategories().size());
        dashboard.put("users", userService.getAllUsers());
        return ResponseEntity.ok(dashboard);
    }
}

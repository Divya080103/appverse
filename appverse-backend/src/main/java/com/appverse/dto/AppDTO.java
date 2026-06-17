package com.appverse.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppDTO {
    private Long id;

    @NotBlank(message = "App name is required")
    @Size(min = 2, max = 100, message = "App name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Description is required")
    @Size(max = 2000, message = "Description must not exceed 2000 characters")
    private String description;

    @NotBlank(message = "Version is required")
    private String currentVersion;

    private String iconUrl;
    private String screenshotUrl;

    private Long downloadCount;
    private Double averageRating;
    private Long developerId;
    private String developerName;

    @NotNull(message = "Category is required")
    private Long categoryId;
    private String categoryName;

    private boolean active;
    private String createdAt;
}

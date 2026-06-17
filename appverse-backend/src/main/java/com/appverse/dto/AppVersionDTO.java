package com.appverse.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppVersionDTO {
    private Long id;

    @NotBlank(message = "Version number is required")
    private String versionNumber;

    private String releaseNotes;
    private String downloadUrl;
    private Long fileSize;
    private Long appId;
    private LocalDateTime releasedAt;
}

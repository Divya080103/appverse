package com.appverse.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "app_versions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Version number is required")
    @Column(nullable = false)
    private String versionNumber;

    @Column(length = 2000)
    private String releaseNotes;

    private String downloadUrl;

    private Long fileSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_id", nullable = false)
    private App app;

    @Column(updatable = false)
    private LocalDateTime releasedAt;

    @PrePersist
    protected void onCreate() {
        releasedAt = LocalDateTime.now();
    }
}

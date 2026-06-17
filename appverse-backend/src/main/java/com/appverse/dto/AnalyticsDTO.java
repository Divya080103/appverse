package com.appverse.dto;

import lombok.*;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalyticsDTO {
    private Long totalDownloads;
    private Long totalApps;
    private Double averageRating;
    private Long totalReviews;
    private Map<String, Long> downloadsByMonth;
    private Map<String, Long> downloadsByApp;
}

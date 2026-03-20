package com.scholife1.dto;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class DashboardStatsDTO {
    private long totalStudents;
    private long totalAdmins;
    private long totalUsers;
    private long totalOrganizations;
    private long unreadMessages;
    private long totalRoles;
}
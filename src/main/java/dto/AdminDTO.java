package com.scholife1.dto;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class AdminDTO {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String employeeNumber;
    private String position;
    private String department;
    private String contactNumber;
    private Long organizationId;
}

package com.scholife1.dto;

import lombok.*;
import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class StudentDTO {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String studentNumber;
    private LocalDate dateOfBirth;
    private String gender;
    private String address;
    private String contactNumber;
    private String guardianName;
    private String guardianContact;
    private String yearLevel;
    private String section;
    private Long organizationId;
    private String status;
}

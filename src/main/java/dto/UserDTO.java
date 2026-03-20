package com.scholife1.dto;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String password;        // only used on create
    private Long organizationId;
    private boolean enabled;
    private String roleName;        // single role for form binding
}

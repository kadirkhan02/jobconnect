package com.jobportal.jobconnect.dto.response;

import com.jobportal.jobconnect.enums.UserRole;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {

    private int id;
    private String name;
    private String email;
    private UserRole role;
    private String phone;
    private String city;
    private String bio;
    private String skills;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
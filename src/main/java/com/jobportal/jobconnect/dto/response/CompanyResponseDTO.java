package com.jobportal.jobconnect.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyResponseDTO {

    private int id;
    private String name;
    private String description;
    private String industry;
    private String website;
    private String city;
    private String email;
    private String phone;
    private int employeeCount;
    private int recruiterId;
    private String recruiterName;
    private String recruiterEmail;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
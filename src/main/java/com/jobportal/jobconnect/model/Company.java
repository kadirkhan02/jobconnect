package com.jobportal.jobconnect.model;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Company {

    private int id;

    @NotBlank(message = "Company naam zaroori hai!")
    private String naam;

    @NotBlank(message = "Description zaroori hai!")
    @Size(min = 10, message = "Thodi zyada description do!")
    private String description;

    @NotBlank(message = "Industry zaroori hai!")
    private String industry;

    private String website;
    private String city;
    private String email;
    private String phone;
    private int employeeCount;
    private int recruiterId;
    private String createdAt;
}
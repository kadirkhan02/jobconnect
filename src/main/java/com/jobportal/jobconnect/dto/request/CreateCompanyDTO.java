package com.jobportal.jobconnect.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCompanyDTO {

    @NotBlank(message = "Company name is required!")
    private String name;

    @NotBlank(message = "Description is required!")
    @Size(min = 10, message = "Please provide a more detailed description!")
    private String description;

    @NotBlank(message = "Industry is required!")
    private String industry;

    private String website;

    @NotBlank(message = "City is required!")
    private String city;

    private String email;
    private String phone;
    private int employeeCount;
}
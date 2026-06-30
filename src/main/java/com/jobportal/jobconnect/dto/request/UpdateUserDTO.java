package com.jobportal.jobconnect.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDTO {

    @Size(min = 2, max = 50, message = "Name must be 2-50 characters!")
    private String name;

    @Pattern(regexp = "^[0-9]{10}$", message = "Please provide a 10 digit phone number!")
    private String phone;

    private String city;
    private String bio;
    private String skills;
}
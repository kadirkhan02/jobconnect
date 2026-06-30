package com.jobportal.jobconnect.dto.request;

import com.jobportal.jobconnect.enums.UserRole;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDTO {

    @NotBlank(message = "Name is required!")
    @Size(min = 2, max = 50, message = "Name must be 2-50 characters!")
    private String name;

    @NotBlank(message = "Email is required!")
    @Email(message = "Please provide a valid email!")
    private String email;

    @NotBlank(message = "Password is required!")
    @Size(min = 6, message = "Password must be at least 6 characters!")
    private String password;

    @NotNull(message = "Role is required!")
    private UserRole role;

    @Pattern(regexp = "^[0-9]{10}$", message = "Please provide a 10 digit phone number!")
    private String phone;

    private String city;
}
package com.jobportal.jobconnect.model;

import com.jobportal.jobconnect.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private int ID;
    @NotBlank(message = "Name is required")
    @Size(min = 4,max = 100,message = "Between 2-100 character required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "/*gmail.com")
    private String email;

    @NotBlank(message = "Please enter password")
    @Size(min = 6, message = "Password kam se kam 6 characters ka hona chahiye!")
    private String password;

    @Pattern(regexp = "^[0-9]{10}",message = "required 10 numbers")
    private String Phone;

    private String CreatedAt;
    private UserRole role;
    private String city;
    private String bio;
    private String skills;
    private boolean active = true;
}

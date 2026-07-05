package com.jobportal.jobconnect.model;

import com.jobportal.jobconnect.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name= "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;

    @NotBlank(message = "Name is required")
    @Size(min = 4,max = 100,message = "Between 2-100 character required")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "/*gmail.com")
    @Column(nullable = false)
    private String email;

    @NotBlank(message = "Please enter password")
    @Size(min = 6, message = "Password kam se kam 6 characters ka hona chahiye!")
    @Column(nullable = false)
    private String password;

    @Pattern(regexp = "^[0-9]{10}",message = "required 10 numbers")
    private String Phone;
    @Column(nullable = false)
    private String CreatedAt;

    @NotNull(message = "Required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    private String city;
    private String bio;
    private String skills;
    @Column(nullable = false)
    private boolean active = true;
}

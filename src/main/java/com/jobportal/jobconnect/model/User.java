package com.jobportal.jobconnect.model;

import com.jobportal.jobconnect.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name= "users")
@ToString(exclude = {"companies", "applications"})
@EqualsAndHashCode(exclude = {"companies", "applications"})
public class User extends BaseEnity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

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


    @NotNull(message = "Required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    private String city;
    private String bio;
    private String skills;
    @Column(nullable = false)
    private boolean active = true;

    @OneToMany(mappedBy = "recruiter", fetch = FetchType.LAZY)
    private List<Company> companies;

    @OneToMany(mappedBy = "applicant", fetch = FetchType.LAZY)
    private List<JobApplication> applications;


}

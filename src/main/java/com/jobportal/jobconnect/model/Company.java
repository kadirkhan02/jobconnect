package com.jobportal.jobconnect.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "companies")
@ToString(exclude = {"recruiter", "jobs"})
@EqualsAndHashCode(exclude = {"recruiter", "jobs"})

public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Company naam zaroori hai!")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Description is required!")
    @Size(min = 10, message = "Please provide a more detailed description!!")
    @Column(nullable = false)
    private String description;

    @NotBlank(message = "Industry is rquired ")
    @Column(nullable = false)
    private String industry;

    private String website;
    private String city;
    private String email;
    private String phone;
    private int employeeCount;

    @Column(nullable = false)
    private String createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruiter_id",nullable = false)
    private User recruiter;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private List<Job> jobs;

}
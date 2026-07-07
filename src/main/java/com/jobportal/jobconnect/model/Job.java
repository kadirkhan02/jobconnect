package com.jobportal.jobconnect.model;
import com.jobportal.jobconnect.enums.JobType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "jobs")

@ToString(exclude = {"company", "postedBy", "applications"})
@EqualsAndHashCode(exclude = {"company", "postedBy", "applications"})

public class Job extends BaseEnity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Job title is required!")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "Description is required!")
    @Size(min = 20, message = "Please provide a more detailed description!")
    @Column(nullable = false , columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String requirements;

    @NotNull(message = "Job type is required!")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobType jobType;

    @NotBlank(message = "Location is required!")
    @Column(nullable = false)
    private String location;

    @Min(value = 0, message = "Salary can not be negative")
    private double salaryMin;

    @Min(value = 0, message = "Salary can not be in negative")
    private double salaryMax;

    private String experience;


    @Column(nullable = false)
    private boolean active = true;

    private String deadline;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="company_id",nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="posted_by_id",nullable = false)
    private User postedBy;

    @OneToMany(mappedBy = "job", fetch = FetchType.LAZY)
    private List<JobApplication> applications;
}

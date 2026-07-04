package com.jobportal.jobconnect.model;
import com.jobportal.jobconnect.enums.JobType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "jobs")
public class Job {

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
    private int companyId;
    @Column(nullable = false)
    private int postedById;
    @Column(nullable = false)
    private boolean active = true;
    @Column(nullable = false)
    private String createdAt;
    private String deadline;
}

package com.jobportal.jobconnect.model;

import com.jobportal.jobconnect.enums.ApplicationStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "job_applications")
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "Job id is required!")
    @Min(value = 1, message = "Please provide a valid job id!")
    @Column(nullable = false)
    private int jobId;

    @NotNull(message = "Applicant id is required!")
    @Min(value = 1, message = "Please provide a valid applicant id!")
    @Column(nullable = false)
    private int applicantId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status = ApplicationStatus.APPLIED;

    @Size(max = 1000, message = "Cover letter 1000 characters se zyada nahi!")
    @Column(columnDefinition = "TEXT")
    private String coverLetter;

    private String resumeLink;

    @Column(updatable = false)
    private String appliedAt;
    private String updatedAt;
}
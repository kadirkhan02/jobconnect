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
@Table(name = "job_applications",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"job_id", "applicant_id"}))
@ToString(exclude = {"job", "applicant"})
@EqualsAndHashCode(exclude = {"job", "applicant"})
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // ❌ private int jobId;       → HATA DO
    // ❌ private int applicantId; → HATA DO

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status = ApplicationStatus.APPLIED;

    @Size(max = 1000, message = "Cover letter 1000 characters max!")
    @Column(columnDefinition = "TEXT")
    private String coverLetter;

    private String resumeLink;

    @Column(updatable = false)
    private String appliedAt;

    private String updatedAt;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicant_id", nullable = false)
    private User applicant;
}
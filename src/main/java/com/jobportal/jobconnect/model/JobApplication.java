package com.jobportal.jobconnect.model;

import com.jobportal.jobconnect.enums.ApplicationStatus;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobApplication {

    private int id;

    @NotNull(message = "Job id zaroori hai!")
    @Min(value = 1, message = "Valid job id do!")
    private int jobId;

    @NotNull(message = "Applicant id zaroori hai!")
    @Min(value = 1, message = "Valid user id do!")
    private int applicantId;

    private ApplicationStatus status = ApplicationStatus.APPLIED;

    @Size(max = 1000, message = "Cover letter 1000 characters se zyada nahi!")
    private String coverLetter;

    private String resumeLink;
    private String appliedAt;
    private String updatedAt;
}
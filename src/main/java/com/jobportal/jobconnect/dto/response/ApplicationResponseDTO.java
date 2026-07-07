package com.jobportal.jobconnect.dto.response;

import com.jobportal.jobconnect.enums.ApplicationStatus;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationResponseDTO {

    private int id;
    private int jobId;
    private String jobTitle;
    private String jobLocation;
    private int applicantId;
    private String applicantName;
    private String applicantEmail;
    private ApplicationStatus status;
    private String coverLetter;
    private String resumeLink;
    private LocalDateTime appliedAt;
    private LocalDateTime updatedAt;

}
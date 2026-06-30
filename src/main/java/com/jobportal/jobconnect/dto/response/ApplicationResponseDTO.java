package com.jobportal.jobconnect.dto.response;

import com.jobportal.jobconnect.enums.ApplicationStatus;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationResponseDTO {

    private int id;
    private int jobId;
    private int applicantId;
    private ApplicationStatus status;
    private String coverLetter;
    private String resumeLink;
    private String appliedAt;
    private String updatedAt;
}
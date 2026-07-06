package com.jobportal.jobconnect.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateApplicationDTO {

    @NotNull(message = "Job id is required!")
    @Min(value = 1, message = "Please provide a valid job id!")
    private int jobId;

//    @NotNull(message = "Applicant id is required!")
//    @Min(value = 1, message = "Please provide a valid applicant id!")
//    private int applicantId;

    @Size(max = 1000, message = "Cover letter cannot exceed 1000 characters!")
    private String coverLetter;

    private String resumeLink;
}
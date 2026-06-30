package com.jobportal.jobconnect.dto.request;

import com.jobportal.jobconnect.enums.JobType;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateJobDTO {

    @NotBlank(message = "Job title is required!")
    private String title;

    @NotBlank(message = "Description is required!")
    @Size(min = 20, message = "Please provide a more detailed description!")
    private String description;

    private String requirements;

    @NotNull(message = "Job type is required!")
    private JobType jobType;

    @NotBlank(message = "Location is required!")
    private String location;

    @Min(value = 0, message = "Salary cannot be negative!")
    private double salaryMin;

    @Min(value = 0, message = "Salary cannot be negative!")
    private double salaryMax;

    private String experience;

    @Min(value = 1, message = "Please provide a valid company id!")
    private int companyId;

    private String deadline;
}
package com.jobportal.jobconnect.model;
import com.jobportal.jobconnect.enums.JobType;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Job {

    private int id;

    @NotBlank(message = "Job title zaroori hai!")
    private String title;

    @NotBlank(message = "Description zaroori hai!")
    @Size(min = 20, message = "Thodi detailed description do!")
    private String description;

    private String requirements;

    @NotNull(message = "Job type zaroori hai!")
    private JobType jobType;

    @NotBlank(message = "Location zaroori hai!")
    private String location;

    @Min(value = 0, message = "Salary negative nahi ho sakti!")
    private double salaryMin;

    @Min(value = 0, message = "Salary negative nahi ho sakti!")
    private double salaryMax;

    private String experience;
    private int companyId;
    private int postedById;
    private boolean active = true;
    private String createdAt;
    private String deadline;
}

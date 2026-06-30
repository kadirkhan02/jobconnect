package com.jobportal.jobconnect.dto.response;

import com.jobportal.jobconnect.enums.JobType;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobResponseDTO {

    private int id;
    private String title;
    private String description;
    private String requirements;
    private JobType jobType;
    private String location;
    private double salaryMin;
    private double salaryMax;
    private String experience;
    private int companyId;
    private int postedById;
    private boolean active;
    private String createdAt;
    private String deadline;
}
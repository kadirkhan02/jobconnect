package com.jobportal.jobconnect.dto.response;

import com.jobportal.jobconnect.enums.JobType;
import lombok.*;

import java.time.LocalDateTime;

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

    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String deadline;

    private int companyId;
    private String companyName;
    private String companyCity;

    private int postedById;
    private String postedByName;
}
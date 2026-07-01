package com.jobportal.jobconnect.service;

import com.jobportal.jobconnect.dto.request.CreateJobDTO;
import com.jobportal.jobconnect.dto.response.JobResponseDTO;
import com.jobportal.jobconnect.enums.JobType;

import java.util.List;

public interface JobService {
    JobResponseDTO create(CreateJobDTO requestDTO, int postedById);
    JobResponseDTO       getById(int id);
    List<JobResponseDTO> getAll();
    List<JobResponseDTO> search(String title, String location,
                                JobType jobType, String experience);
    List<JobResponseDTO> getByCompanyId(int companyId);
    JobResponseDTO       update(int id, CreateJobDTO updateDTO);
    JobResponseDTO       toggleStatus(int id);
    void                 delete(int id);
}

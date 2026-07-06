package com.jobportal.jobconnect.service;

import com.jobportal.jobconnect.dto.request.CreateJobDTO;
import com.jobportal.jobconnect.dto.response.JobResponseDTO;
import com.jobportal.jobconnect.enums.JobType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface JobService {
    JobResponseDTO create(CreateJobDTO requestDTO, int postedById);
    JobResponseDTO       getById(int id);
    Page<JobResponseDTO> getAll(Pageable pageable);
    List<JobResponseDTO> search(String title, String location,
                                JobType jobType, String experience,Double minSalary,Double maxSakary);
    List<JobResponseDTO> getByCompanyId(int companyId);
    JobResponseDTO       update(int id, CreateJobDTO updateDTO);
    JobResponseDTO       toggleStatus(int id);
    void                 delete(int id);
}

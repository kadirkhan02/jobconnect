package com.jobportal.jobconnect.service;

import com.jobportal.jobconnect.dto.request.CreateApplicationDTO;
import com.jobportal.jobconnect.dto.response.ApplicationResponseDTO;
import com.jobportal.jobconnect.enums.ApplicationStatus;

import java.util.List;

public interface ApplicationService {

    ApplicationResponseDTO       apply(CreateApplicationDTO requestDTO);
    ApplicationResponseDTO       getById(int id);
    List<ApplicationResponseDTO> getByJobId(int jobId);
    List<ApplicationResponseDTO> getByApplicantId(int applicantId);
    ApplicationResponseDTO       updateStatus(int id, ApplicationStatus status);
    void                         withdraw(int id);
}
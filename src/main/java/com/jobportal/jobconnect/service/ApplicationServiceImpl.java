package com.jobportal.jobconnect.service;

import com.jobportal.jobconnect.dto.request.CreateApplicationDTO;
import com.jobportal.jobconnect.dto.response.ApplicationResponseDTO;
import com.jobportal.jobconnect.enums.ApplicationStatus;
import com.jobportal.jobconnect.exception.BadRequestException;
import com.jobportal.jobconnect.exception.ResourceNotFoundException;
import com.jobportal.jobconnect.model.JobApplication;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
public class ApplicationServiceImpl implements ApplicationService{

    @Autowired
            private ModelMapper modelMapper;
    List<JobApplication> jobApplications=new ArrayList<>();
    private int nextId=1;
    @Override
    public ApplicationResponseDTO apply(CreateApplicationDTO requestDTO) {

        boolean check =jobApplications.stream().anyMatch(c->c.getApplicantId()== requestDTO.getApplicantId() &&
                                                         c.getJobId() == requestDTO.getJobId());

        if(check)
        {
            throw new BadRequestException("You have already applied for this job");
        }
        JobApplication jobApplication=modelMapper.map(requestDTO,JobApplication.class);

        jobApplication.setApplicantId(nextId);
        jobApplication.setAppliedAt(LocalDateTime.now().toString());
        jobApplication.setStatus(ApplicationStatus.APPLIED);
        jobApplication.setUpdatedAt(LocalDateTime.now().toString());
        jobApplications.add(jobApplication);

        log.info("New application submitted - jobId: {}, applicantId: {}",
                requestDTO.getJobId(), requestDTO.getApplicantId());

        return modelMapper.map(jobApplication,ApplicationResponseDTO.class);
    }

    @Override
    public ApplicationResponseDTO getById(int id) {
        JobApplication application = findApplicationById(id);
        return modelMapper.map(application, ApplicationResponseDTO.class);


    }

    private JobApplication findApplicationById(int id) {
        return jobApplications.stream()
                .filter(a -> a.getId() == id)
                .findFirst()
                .orElseThrow(() ->
                        new ResourceNotFoundException("Application", id));
    }

    @Override
    public List<ApplicationResponseDTO> getByJobId(int jobId) {

        return jobApplications.stream().filter(c->c.getJobId()==jobId).map(c->
                        modelMapper.map(c,ApplicationResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ApplicationResponseDTO> getByApplicantId(int applicantId) {
        return jobApplications.stream().filter(
                c->c.getApplicantId()==applicantId
        ).map(c->modelMapper.map(c,ApplicationResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public ApplicationResponseDTO updateStatus(int id, ApplicationStatus status) {

        JobApplication jobApplication=findApplicationById(id);
        jobApplication.setStatus(status);
        jobApplication.setUpdatedAt(LocalDateTime.now().toString());
        log.info("Application status updated - id: {}, status: {}", id, status);
        return modelMapper.map(jobApplication,ApplicationResponseDTO.class);
    }

    @Override
    public void withdraw(int id) {
        JobApplication application = findApplicationById(id);
        jobApplications.remove(application);
        log.info("Application withdrawn - id: {}", id);
    }
}

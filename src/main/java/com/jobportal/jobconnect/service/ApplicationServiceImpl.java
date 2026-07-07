package com.jobportal.jobconnect.service;

import com.jobportal.jobconnect.dto.request.CreateApplicationDTO;
import com.jobportal.jobconnect.dto.response.ApplicationResponseDTO;
import com.jobportal.jobconnect.enums.ApplicationStatus;
import com.jobportal.jobconnect.exception.BadRequestException;
import com.jobportal.jobconnect.exception.ResourceNotFoundException;
import com.jobportal.jobconnect.model.Job;
import com.jobportal.jobconnect.model.JobApplication;
import com.jobportal.jobconnect.model.User;
import com.jobportal.jobconnect.repository.ApplicationRepository;
import com.jobportal.jobconnect.repository.JobRepository;
import com.jobportal.jobconnect.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ApplicationServiceImpl implements ApplicationService {

    @Autowired private ApplicationRepository applicationRepository;
    @Autowired private JobRepository         jobRepository;
    @Autowired private UserRepository        userRepository;
    @Autowired private ModelMapper           modelMapper;

    @Override
    public ApplicationResponseDTO apply(CreateApplicationDTO dto,
                                        int applicantId) {
        Job job = jobRepository.findById(dto.getJobId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Job", dto.getJobId()));

        User applicant = userRepository.findById(applicantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User", applicantId));

        if (applicationRepository.existsByJobIdAndApplicantId(
                dto.getJobId(), applicantId))
            throw new BadRequestException(
                    "You have already applied for this job!");

        // ModelMapper — basic fields
        JobApplication app = modelMapper.map(dto, JobApplication.class);

        // Manual — relationships
        app.setJob(job);
        app.setApplicant(applicant);
        app.setStatus(ApplicationStatus.APPLIED);
       // app.setAppliedAt(LocalDateTime.now().toString());
       // app.setUpdatedAt(LocalDateTime.now().toString());

        JobApplication saved = applicationRepository.save(app);
        log.info("Application submitted - jobId:{}, applicantId:{}",
                dto.getJobId(), applicantId);
        return mapToDTO(saved);
    }

    @Override
    public ApplicationResponseDTO getById(int id) {
        return mapToDTO(findById(id));
    }

    @Override
    public List<ApplicationResponseDTO> getByJobId(int jobId) {
        return applicationRepository.findByJobId(jobId)
                .stream().map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ApplicationResponseDTO> getByApplicantId(
            int applicantId) {
        return applicationRepository.findByApplicantId(applicantId)
                .stream().map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ApplicationResponseDTO updateStatus(int id,
                                               ApplicationStatus status) {
        JobApplication app = findById(id);
        app.setStatus(status);
       // app.setUpdatedAt(LocalDateTime.now().toString());
        return mapToDTO(applicationRepository.save(app));
    }

    @Override
    public void withdraw(int id) {
        applicationRepository.delete(findById(id));
        log.info("Application withdrawn id: {}", id);
    }

    // Hybrid mapping
    private ApplicationResponseDTO mapToDTO(JobApplication a) {
        // ModelMapper — basic fields
        ApplicationResponseDTO dto = modelMapper.map(a,
                ApplicationResponseDTO.class);
        // Manual — relationship fields
        if (a.getJob() != null) {
            dto.setJobId(a.getJob().getId());
            dto.setJobTitle(a.getJob().getTitle());
            dto.setJobLocation(a.getJob().getLocation());
        }
        if (a.getApplicant() != null) {
            dto.setApplicantId(a.getApplicant().getId());
            dto.setApplicantName(a.getApplicant().getName());
            dto.setApplicantEmail(a.getApplicant().getEmail());
        }
        return dto;
    }

    private JobApplication findById(int id) {
        return applicationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Application", id));
    }
}
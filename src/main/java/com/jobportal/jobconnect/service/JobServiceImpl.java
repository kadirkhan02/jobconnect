package com.jobportal.jobconnect.service;

import com.jobportal.jobconnect.dto.request.CreateJobDTO;
import com.jobportal.jobconnect.dto.response.JobResponseDTO;
import com.jobportal.jobconnect.enums.JobType;
import com.jobportal.jobconnect.exception.ResourceNotFoundException;
import com.jobportal.jobconnect.model.Job;
import com.jobportal.jobconnect.repository.JobRepository;
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
public class JobServiceImpl implements JobService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JobRepository jobRepository;

    //private List<Job> jobs = new ArrayList<>();
    private int nextId = 1;

    @Override
    public JobResponseDTO create(CreateJobDTO requestDTO, int postedById) {
        Job job = modelMapper.map(requestDTO, Job.class);

        job.setPostedById(postedById);
        job.setActive(true);
        job.setCreatedAt(LocalDateTime.now().toString());

        Job savedjob =jobRepository.save(job);
        log.info("New job posted: {}", job.getTitle());
        return modelMapper.map(savedjob, JobResponseDTO.class);
    }

    @Override
    public JobResponseDTO getById(int id) {
        Job job = findJobById(id);
        return modelMapper.map(job, JobResponseDTO.class);
    }

    @Override
    public List<JobResponseDTO> getAll() {
        log.info("Fetching all active jobs");
        return jobRepository.findAll().stream()
                .map(j -> modelMapper.map(j, JobResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<JobResponseDTO> search(String title, String location,
                                       JobType jobType, String experience) {
        log.info("Searching jobs - title: {}, location: {}, type: {}",
                title, location, jobType);
        return jobRepository.searchJobs(title, location, jobType, experience)
                .stream().map(j->modelMapper.map(j,JobResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<JobResponseDTO> getByCompanyId(int companyId) {
        return jobRepository.findByCompanyId(companyId)
                .stream()
                .map(j -> modelMapper.map(j, JobResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public JobResponseDTO update(int id, CreateJobDTO updateDTO) {
        Job job = findJobById(id);

        if (updateDTO.getTitle()        != null) job.setTitle(updateDTO.getTitle());
        if (updateDTO.getDescription()  != null) job.setDescription(updateDTO.getDescription());
        if (updateDTO.getRequirements() != null) job.setRequirements(updateDTO.getRequirements());
        if (updateDTO.getJobType()      != null) job.setJobType(updateDTO.getJobType());
        if (updateDTO.getLocation()     != null) job.setLocation(updateDTO.getLocation());
        if (updateDTO.getExperience()   != null) job.setExperience(updateDTO.getExperience());
        if (updateDTO.getDeadline()     != null) job.setDeadline(updateDTO.getDeadline());
        job.setSalaryMin(updateDTO.getSalaryMin());
        job.setSalaryMax(updateDTO.getSalaryMax());

        Job updatedjob=jobRepository.save(job);
        log.info("Job updated with id: {}", id);
        return modelMapper.map(updatedjob, JobResponseDTO.class);
    }

    @Override
    public JobResponseDTO toggleStatus(int id) {
        Job job = findJobById(id);
        job.setActive(!job.isActive());
        Job updatedjob=jobRepository.save(job);
        log.info("Job status toggled - id: {}, active: {}", id, job.isActive());
        return modelMapper.map(updatedjob, JobResponseDTO.class);
    }

    @Override
    public void delete(int id) {
        Job job = findJobById(id);
        jobRepository.delete(job);
        log.info("Job deleted with id: {}", id);
    }

    private Job findJobById(int id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job", id));
    }
}
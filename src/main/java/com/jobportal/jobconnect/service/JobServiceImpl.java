package com.jobportal.jobconnect.service;

import com.jobportal.jobconnect.dto.request.CreateJobDTO;
import com.jobportal.jobconnect.dto.response.JobResponseDTO;
import com.jobportal.jobconnect.enums.JobType;
import com.jobportal.jobconnect.exception.ResourceNotFoundException;
import com.jobportal.jobconnect.model.Company;
import com.jobportal.jobconnect.model.Job;
import com.jobportal.jobconnect.model.User;
import com.jobportal.jobconnect.repository.CompanyRepository;
import com.jobportal.jobconnect.repository.JobRepository;
import com.jobportal.jobconnect.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    //private List<Job> jobs = new ArrayList<>();
    private int nextId = 1;


    private JobResponseDTO mapToDTO(Job job)
    {
        JobResponseDTO jobResponseDTO=modelMapper.map(job,JobResponseDTO.class);

        if(job.getCompany()!=null)
        {
            jobResponseDTO.setCompanyId(job.getCompany().getId());
            jobResponseDTO.setCompanyName(job.getCompany().getName());
            jobResponseDTO.setCompanyCity(job.getCompany().getCity());
        }
        if(job.getPostedBy()!=null)
        {
            jobResponseDTO.setPostedById(job.getPostedBy().getId());
            jobResponseDTO.setPostedByName(job.getPostedBy().getName());
        }
        return jobResponseDTO;
    }

    @Override
    public JobResponseDTO create(CreateJobDTO requestDTO, int postedById) {

        Company company=companyRepository.findById(requestDTO.getCompanyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found", requestDTO.getCompanyId()));

        User user=userRepository.findById(postedById)
                .orElseThrow(()->new ResourceNotFoundException(
                        "User",postedById
                ));

        Job job = modelMapper.map(requestDTO, Job.class);

        job.setCompany(company);
        job.setPostedBy(user);
        job.setActive(true);
        //job.setCreatedAt(LocalDateTime.now().toString());

        Job savedjob =jobRepository.save(job);
        log.info("New job posted: {}", job.getTitle());

        return mapToDTO(savedjob);
    }

    @Override
    public JobResponseDTO getById(int id) {
        Job job = findJobById(id);
        return mapToDTO(job);
    }

    @Override
    public Page<JobResponseDTO> getAll(Pageable pageable) {
        log.info("Fetching all active jobs");
        return jobRepository.findByActiveTrue(pageable)
                .map(this::mapToDTO);
    }

    @Override
    public List<JobResponseDTO> search(String title, String location,
                                       JobType jobType, String experience
                                       ,Double minSalary,Double maxSalary) {
        log.info("Searching jobs - title: {}, location: {}, type: {}",
                title, location, jobType,experience,minSalary,maxSalary);

        return jobRepository.searchJobs(title, location, jobType, experience,minSalary,maxSalary)
                .stream().map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<JobResponseDTO> getByCompanyId(int companyId) {
        return jobRepository.findByCompanyId(companyId)
                .stream()
                .map(this::mapToDTO)
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


        return mapToDTO(job);
    }

    @Override
    public JobResponseDTO toggleStatus(int id) {
        Job job = findJobById(id);
        job.setActive(!job.isActive());

        return mapToDTO(job);
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
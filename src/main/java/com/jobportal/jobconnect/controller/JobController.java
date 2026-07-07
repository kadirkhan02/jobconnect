package com.jobportal.jobconnect.controller;

import com.jobportal.jobconnect.dto.request.CreateJobDTO;
import com.jobportal.jobconnect.dto.response.JobResponseDTO;
import com.jobportal.jobconnect.enums.JobType;
import com.jobportal.jobconnect.response.ApiResponse;
import com.jobportal.jobconnect.service.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(name = "Job", description = "Job posting APIs")
@Slf4j
@RestController
@RequestMapping("/api/v1/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    // POST /api/v1/jobs?postedById=1 - Create new job
    @Operation(summary = "Post new job")
    @PostMapping
    public ResponseEntity<ApiResponse<JobResponseDTO>> create(
            @Valid @RequestBody CreateJobDTO requestDTO,
            @RequestParam int postedById) {
        log.info("Creating job: {}", requestDTO.getTitle());
        JobResponseDTO response = jobService.create(requestDTO, postedById);
        return ResponseEntity.status(201)
                .body(ApiResponse.created(response, "Job posted successfully!"));
    }

    // GET /api/v1/jobs - Get all active jobs
    @Operation(summary = "Get all active jobs with pagination")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<JobResponseDTO>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        Sort.Direction dir=direction.equalsIgnoreCase("asc")?
                Sort.Direction.ASC : Sort.Direction.DESC;

        Pageable pageable= PageRequest.of(page,size,Sort.by(dir,sort));

        log.info("Fetching all jobs");
        Page<JobResponseDTO> jobs = jobService.getAll(pageable);

        return ResponseEntity.ok(
                ApiResponse.success(jobs," jobs found!"));
    }

    // GET /api/v1/jobs/{id} - Get job by id

    @Operation(summary = "Get job by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<JobResponseDTO>> getById(
            @PathVariable int id) {
        log.info("Fetching job with id: {}", id);
        return ResponseEntity.ok(
                ApiResponse.success(jobService.getById(id), "Job found!"));
    }

    // GET /api/v1/jobs/search?title=&location=&jobType=&experience=
    @Operation(summary = "Search jobs with filters")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<JobResponseDTO>>> search(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) JobType jobType,
            @RequestParam(required = false) String experience,
            @RequestParam(required = false) Double minSalary,
            @RequestParam(required = false) Double maxSalary){
        log.info("Searching jobs");
        List<JobResponseDTO> results = jobService.search(
                title, location, jobType, experience,minSalary,maxSalary);
        return ResponseEntity.ok(
                ApiResponse.success(results, results.size() + " jobs found!"));
    }

    // GET /api/v1/jobs/company/{companyId} - Jobs by company
    @Operation(summary = "Get jobs by company")
    @GetMapping("/company/{companyId}")
    public ResponseEntity<ApiResponse<List<JobResponseDTO>>> getByCompany(
            @PathVariable int companyId) {
        log.info("Fetching jobs for company: {}", companyId);
        List<JobResponseDTO> jobs = jobService.getByCompanyId(companyId);
        return ResponseEntity.ok(
                ApiResponse.success(jobs, jobs.size() + " jobs found!"));
    }

    // PUT /api/v1/jobs/{id} - Update job
    @Operation(summary = "Update job")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<JobResponseDTO>> update(
            @PathVariable int id,
            @Valid @RequestBody CreateJobDTO updateDTO) {
        log.info("Updating job with id: {}", id);
        return ResponseEntity.ok(
                ApiResponse.success(jobService.update(id, updateDTO),
                        "Job updated successfully!"));
    }

    // PATCH /api/v1/jobs/{id}/status - Toggle job active/inactive
    @Operation(summary = "Toggle job active/inactive status")
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<JobResponseDTO>> toggleStatus(
            @PathVariable int id) {
        log.info("Toggling status for job id: {}", id);
        return ResponseEntity.ok(
                ApiResponse.success(jobService.toggleStatus(id),
                        "Job status updated!"));
    }

    // DELETE /api/v1/jobs/{id} - Delete job
    @Operation(summary = "Delete job")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable int id) {
        log.info("Deleting job with id: {}", id);
        jobService.delete(id);
        return ResponseEntity.ok(
                ApiResponse.success(null, "Job deleted successfully!"));
    }
}
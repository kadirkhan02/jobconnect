package com.jobportal.jobconnect.controller;

import com.jobportal.jobconnect.dto.request.CreateApplicationDTO;
import com.jobportal.jobconnect.dto.response.ApplicationResponseDTO;
import com.jobportal.jobconnect.enums.ApplicationStatus;
import com.jobportal.jobconnect.response.ApiResponse;
import com.jobportal.jobconnect.service.ApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(name = "Application", description = "Job application APIs")
@Slf4j
@RestController
@RequestMapping("/api/v1/applications")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    // POST /api/v1/applications - Apply for a job
    @Operation(summary = "Apply for a job")
    @PostMapping
    public ResponseEntity<ApiResponse<ApplicationResponseDTO>> apply(
            @Valid @RequestBody CreateApplicationDTO requestDTO,
            @RequestParam int applicantId) {
        log.info("New application - jobId: {}, applicantId: {}",
                requestDTO.getJobId(),applicantId);
        ApplicationResponseDTO response = applicationService.apply(requestDTO,applicantId);
        return ResponseEntity.status(201)
                .body(ApiResponse.created(response,
                        "Application submitted successfully!"));
    }

    // GET /api/v1/applications/{id} - Get application by id
    @Operation(summary = "Get application by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ApplicationResponseDTO>> getById(
            @PathVariable int id) {
        log.info("Fetching application with id: {}", id);
        return ResponseEntity.ok(
                ApiResponse.success(
                        applicationService.getById(id), "Application found!"));
    }

    // GET /api/v1/applications/job/{jobId} - All applications for a job
    @Operation(summary = "Get all applications for a job")
    @GetMapping("/job/{jobId}")
    public ResponseEntity<ApiResponse<List<ApplicationResponseDTO>>> getByJobId(
            @PathVariable int jobId) {
        log.info("Fetching applications for job: {}", jobId);
        List<ApplicationResponseDTO> apps =
                applicationService.getByJobId(jobId);
        return ResponseEntity.ok(
                ApiResponse.success(apps, apps.size() + " applications found!"));
    }

    // GET /api/v1/applications/user/{applicantId} - User ke saare applications
    @Operation(summary = "Get all applications by user")
    @GetMapping("/user/{applicantId}")
    public ResponseEntity<ApiResponse<List<ApplicationResponseDTO>>> getByApplicant(
            @PathVariable int applicantId) {
        log.info("Fetching applications for applicant: {}", applicantId);
        List<ApplicationResponseDTO> apps =
                applicationService.getByApplicantId(applicantId);
        return ResponseEntity.ok(
                ApiResponse.success(apps, apps.size() + " applications found!"));
    }

    // PATCH /api/v1/applications/{id}/status?status=SHORTLISTED
    @Operation(summary = "Update application status")
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<ApplicationResponseDTO>> updateStatus(
            @PathVariable int id,
            @RequestParam ApplicationStatus status) {
        log.info("Updating application status - id: {}, status: {}", id, status);
        return ResponseEntity.ok(
                ApiResponse.success(
                        applicationService.updateStatus(id, status),
                        "Application status updated!"));
    }

    // DELETE /api/v1/applications/{id} - Withdraw application
    @Operation(summary = "Withdraw application")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> withdraw(
            @PathVariable int id) {
        log.info("Withdrawing application with id: {}", id);
        applicationService.withdraw(id);
        return ResponseEntity.ok(
                ApiResponse.success(null, "Application withdrawn successfully!"));
    }
}
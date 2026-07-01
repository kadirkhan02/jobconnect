package com.jobportal.jobconnect.controller;


import com.jobportal.jobconnect.dto.request.CreateCompanyDTO;
import com.jobportal.jobconnect.dto.response.CompanyResponseDTO;
import com.jobportal.jobconnect.response.ApiResponse;
import com.jobportal.jobconnect.service.CompanyService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/companies")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @PostMapping
    public ResponseEntity<ApiResponse<CompanyResponseDTO>>create(
            @Valid @RequestBody CreateCompanyDTO requestDTO,
            @RequestParam int recruiterId
            )
    {
        log.info("Creating company: {}", requestDTO.getName());
        CompanyResponseDTO companyResponseDTO=companyService.create(requestDTO,recruiterId);

        return ResponseEntity.status(201)
                .body(ApiResponse.created(companyResponseDTO,"Comapny created Successfully"));

    }
    @GetMapping
    public ResponseEntity<ApiResponse<List<CompanyResponseDTO>>> getAll() {
        log.info("Fetching all companies");
        List<CompanyResponseDTO> companies = companyService.getAll();
        return ResponseEntity.ok(
                ApiResponse.success(companies, companies.size() + " companies found!"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CompanyResponseDTO>> getById(
            @PathVariable int id
    ){
        CompanyResponseDTO companyResponseDTO=companyService.getById(id);
        return ResponseEntity.ok().body(ApiResponse.success(companyResponseDTO,"Company found!"));
    }

    // GET /api/v1/companies/search?name=&city=&industry= - Search companies
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<CompanyResponseDTO>>> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String industry) {
        log.info("Searching companies");
        List<CompanyResponseDTO> results = companyService.search(name, city, industry);
        return ResponseEntity.ok(
                ApiResponse.success(results, results.size() + " companies matched!"));
    }

    // GET /api/v1/companies/recruiter/{recruiterId} - Companies by recruiter
    @GetMapping("/recruiter/{recruiterId}")
    public ResponseEntity<ApiResponse<List<CompanyResponseDTO>>> getByRecruiterId(
            @PathVariable int recruiterId) {
        log.info("Fetching companies for recruiter: {}", recruiterId);
        List<CompanyResponseDTO> companies = companyService.getByRecruiterId(recruiterId);
        return ResponseEntity.ok(
                ApiResponse.success(companies, companies.size() + " companies found!"));
    }

    // PUT /api/v1/companies/{id} - Update company
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CompanyResponseDTO>> update(
            @PathVariable int id,
            @Valid @RequestBody CreateCompanyDTO updateDTO) {
        log.info("Updating company with id: {}", id);
        return ResponseEntity.ok(
                ApiResponse.success(companyService.update(id, updateDTO),
                        "Company updated successfully!"));
    }

    // DELETE /api/v1/companies/{id} - Delete company
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable int id) {
        log.info("Deleting company with id: {}", id);
        companyService.delete(id);
        return ResponseEntity.ok(
                ApiResponse.success(null, "Company deleted successfully!"));
    }
}


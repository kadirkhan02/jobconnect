package com.jobportal.jobconnect.service;

import com.jobportal.jobconnect.dto.request.CreateCompanyDTO;
import com.jobportal.jobconnect.dto.response.CompanyResponseDTO;

import java.util.List;

public interface CompanyService {
    CompanyResponseDTO create(CreateCompanyDTO requestDTO ,int recruiterId);
    CompanyResponseDTO getById(int id);
    List<CompanyResponseDTO> getAll();
    List<CompanyResponseDTO> search(String name,String city,String industry);
    List<CompanyResponseDTO> getByRecruiterId(int recruiterId);
    CompanyResponseDTO update(int id,CreateCompanyDTO updateDTO);
    void delete(int id);
}

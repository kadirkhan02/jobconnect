package com.jobportal.jobconnect.service;

import com.jobportal.jobconnect.dto.request.CreateCompanyDTO;
import com.jobportal.jobconnect.dto.response.CompanyResponseDTO;
import com.jobportal.jobconnect.exception.ResourceNotFoundException;
import com.jobportal.jobconnect.model.Company;
import com.jobportal.jobconnect.model.User;
import com.jobportal.jobconnect.repository.CompanyRepository;
import com.jobportal.jobconnect.repository.UserRepository;
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
public class CompanyServiceImpl implements CompanyService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

   // private List<Company>companies = new ArrayList<>();

    @Autowired
    private CompanyRepository companyRepository;
    //private int nextId=1;

    private CompanyResponseDTO mapTODTO(Company c)
    {
        CompanyResponseDTO dto=modelMapper.map(c,CompanyResponseDTO.class);

        if(c.getRecruiter() !=null)
        {
            dto.setRecruiterId(c.getRecruiter().getId());
            dto.setRecruiterName(c.getRecruiter().getName());
            dto.setRecruiterEmail(c.getRecruiter().getEmail());
        }
        return dto;
    }

    @Override
    public CompanyResponseDTO create(CreateCompanyDTO requestDTO, int recruiterId) {


        User recruiter = userRepository.findById(recruiterId).orElseThrow(()->new ResourceNotFoundException("Recruiter id not found",recruiterId));


        Company company =modelMapper.map(requestDTO,Company.class);
        company.setRecruiter(recruiter);
       // company.setCreatedAt(LocalDateTime.now().toString());

        Company savedcompany=companyRepository.save(company);
        log.info("Company is added with name {}",savedcompany.getName());

        return mapTODTO(savedcompany);
    }

    @Override
    public CompanyResponseDTO getById(int id) {

        Company company= findById(id);
        return mapTODTO(company);
    }

    private Company findById(int id) {

        return companyRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Comany",id));

    }

    @Override
    public List<CompanyResponseDTO> getAll() {


        return companyRepository.findAll().stream()
                .map(this::mapTODTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CompanyResponseDTO> search(String name, String city, String industry) {
        log.info("Searching companies - name: {}, city: {}, industry: {}",
                name, city, industry);

        return companyRepository.searchCompanies(name, city, industry)
                .stream().map(this::mapTODTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CompanyResponseDTO> getByRecruiterId(int recruiterId) {


        return companyRepository.findByRecruiterId(recruiterId).stream()
                .map(this::mapTODTO)
                .collect(Collectors.toList());
    }

    @Override
    public CompanyResponseDTO update(int id, CreateCompanyDTO updateDTO) {

        Company company =findById(id);
        if(updateDTO.getName() != null) company.setName(updateDTO.getName());
        if (updateDTO.getDescription() != null) company.setDescription(updateDTO.getDescription());
        if (updateDTO.getIndustry()    != null) company.setIndustry(updateDTO.getIndustry());
        if (updateDTO.getWebsite()     != null) company.setWebsite(updateDTO.getWebsite());
        if (updateDTO.getCity()        != null) company.setCity(updateDTO.getCity());
        if (updateDTO.getEmail()       != null) company.setEmail(updateDTO.getEmail());
        if (updateDTO.getPhone()       != null) company.setPhone(updateDTO.getPhone());


        log.info("Company updated with id: {}", id);
        return mapTODTO(company);

    }

    @Override
    public void delete(int id) {
        Company company = findById(
                id);
        companyRepository.delete(company);
        log.info("Company deleted with id: {}", id);

    }
}

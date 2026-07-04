package com.jobportal.jobconnect.service;

import com.jobportal.jobconnect.dto.request.CreateCompanyDTO;
import com.jobportal.jobconnect.dto.response.CompanyResponseDTO;
import com.jobportal.jobconnect.exception.ResourceNotFoundException;
import com.jobportal.jobconnect.model.Company;
import com.jobportal.jobconnect.repository.CompanyRepository;
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
    private ModelMapper modelMapper;

   // private List<Company>companies = new ArrayList<>();

    @Autowired
    private CompanyRepository companyRepository;
    //private int nextId=1;

    @Override
    public CompanyResponseDTO create(CreateCompanyDTO requestDTO, int recruiterId) {


        Company company =modelMapper.map(requestDTO,Company.class);
        company.setRecruiterId(recruiterId);
        company.setCreatedAt(LocalDateTime.now().toString());

        Company savedcompany=companyRepository.save(company);
        log.info("Company is added with name {}",savedcompany.getName());

        return modelMapper.map(company,CompanyResponseDTO.class);
    }

    @Override
    public CompanyResponseDTO getById(int id) {

        Company company= findById(id);
        return modelMapper.map(company,CompanyResponseDTO.class);
    }

    private Company findById(int id) {

        return companyRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Comany",id));

    }

    @Override
    public List<CompanyResponseDTO> getAll() {


        return companyRepository.findAll().stream()
                .map(u->modelMapper.map(u,CompanyResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<CompanyResponseDTO> search(String name, String city, String industry) {
        log.info("Searching companies - name: {}, city: {}, industry: {}",
                name, city, industry);

        return companyRepository.findAll().stream()
                .filter(c -> name == null ||
                        c.getName().toLowerCase()
                                .contains(name.toLowerCase()))
                .filter(c->name ==null ||
                        c.getCity().equalsIgnoreCase(city))
                .filter(c->industry ==null ||
                        c.getIndustry().equalsIgnoreCase(industry))
                .map(c->modelMapper.map(c,CompanyResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<CompanyResponseDTO> getByRecruiterId(int recruiterId) {


        return companyRepository.findByRecruiterId(recruiterId).stream()
                .map(u->modelMapper.map(u,CompanyResponseDTO.class))
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

        Company savedcompany =companyRepository.save(company);
        log.info("Company updated with id: {}", id);
        return modelMapper.map(savedcompany, CompanyResponseDTO.class);

    }

    @Override
    public void delete(int id) {
        Company company = findById(
                id);
        companyRepository.delete(company);
        log.info("Company deleted with id: {}", id);

    }
}

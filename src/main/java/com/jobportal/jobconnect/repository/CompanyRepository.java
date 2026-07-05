package com.jobportal.jobconnect.repository;

import com.jobportal.jobconnect.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company,Integer> {

    List<Company> findByRecruiterId(int recruiterId);

    List<Company> findByNameContainingIgnoreCase(String Name);


    List<Company> findByCityIgnoreCase(String city);

    // Search by industry
    List<Company> findByIndustryIgnoreCase(String industry);
}

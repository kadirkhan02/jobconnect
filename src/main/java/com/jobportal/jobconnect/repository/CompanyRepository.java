package com.jobportal.jobconnect.repository;

import com.jobportal.jobconnect.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company,Integer> {

    List<Company> findByRecruiterId(int recruiterId);


    @Query("SELECT c FROM Company c WHERE " +
            "(:name IS NULL OR " +
            "LOWER(c.name) LIKE LOWER(CONCAT('%',:name,'%'))) "+
            "AND (:city IS NULL OR " +
            "LOWER(c.city) = LOWER(:city)) " +
            "AND (:industry IS NULL OR " +
            "LOWER(c.industry) = LOWER(:industry))")
    List<Company> searchCompanies(
            @Param("name") String name,
            @Param("city") String city,
            @Param("industry") String industry
    );

//    List<Company> findByNameContainingIgnoreCase(String Name);
//
//
//    List<Company> findByCityIgnoreCase(String city);
//
//    // Search by industry
//    List<Company> findByIndustryIgnoreCase(String industry);
}

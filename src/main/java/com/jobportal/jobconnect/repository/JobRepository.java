package com.jobportal.jobconnect.repository;

import com.jobportal.jobconnect.enums.JobType;
import com.jobportal.jobconnect.model.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Integer> {


    Page<Job> findByActiveTrue(Pageable pageable);

    //List<Job> findByActiveTrue();
    List<Job> findByCompanyId(int companyId);

    List<Job> findByPostedById(int postedById);

    @Query("SELECT j FROM Job j WHERE j.active = true " +
            "AND (:title IS NULL OR " +
            "LOWER(j.title) LIKE LOWER(CONCAT('%',:title,'%'))) " +
            "AND (:location IS NULL OR " +
            "LOWER(j.location) = LOWER(:location)) " +
            "AND (:jobType IS NULL OR j.jobType = :jobType) " +
            "AND (:experience IS NULL OR " +
            "LOWER(j.experience) = LOWER(:experience)) " +
            "AND (:minSalary IS NULL OR j.salaryMin >= :minSalary) " +
            "AND (:maxSalary IS NULL OR j.salaryMax <= :maxSalary)")
    List<Job> searchJobs(
            @Param("title")     String title,
            @Param("location")  String location,
            @Param("jobType")   JobType jobType,
            @Param("experience") String experience,
            @Param("minSalary") Double minSalary,
            @Param("maxSalary") Double maxSalary
    );


}
package com.jobportal.jobconnect.repository;

import com.jobportal.jobconnect.model.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<JobApplication, Integer> {


    List<JobApplication> findByJobId(int jobId);


    List<JobApplication> findByApplicantId(int applicantId);


    boolean existsByJobIdAndApplicantId(int jobId, int applicantId);
}
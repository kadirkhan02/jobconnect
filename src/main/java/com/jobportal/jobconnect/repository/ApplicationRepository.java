package com.jobportal.jobconnect.repository;

import com.jobportal.jobconnect.model.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ApplicationRepository
        extends JpaRepository<JobApplication, Integer> {

    // job.id se dhundho
    @Query("SELECT a FROM JobApplication a WHERE a.job.id = :jobId")
    List<JobApplication> findByJobId(@Param("jobId") int jobId);

    // applicant.id se dhundho
    @Query("SELECT a FROM JobApplication a WHERE a.applicant.id = :applicantId")
    List<JobApplication> findByApplicantId(
            @Param("applicantId") int applicantId);

    // Duplicate check
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END " +
            "FROM JobApplication a " +
            "WHERE a.job.id = :jobId " +
            "AND a.applicant.id = :applicantId")
    boolean existsByJobIdAndApplicantId(
            @Param("jobId")       int jobId,
            @Param("applicantId") int applicantId);
}
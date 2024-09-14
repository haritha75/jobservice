package com.revature.jobpostservice.repository;

import com.revature.jobpostservice.model.AppliedJobs;
import com.revature.jobpostservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppliedJobsRepository extends JpaRepository<AppliedJobs,Long> {
    List<AppliedJobs> findByUser(User user);

}

package com.revature.jobpostservice.service;

import com.revature.jobpostservice.model.AppliedJobs;
import com.revature.jobpostservice.model.Job;
import com.revature.jobpostservice.model.User;
import com.revature.jobpostservice.repository.AppliedJobsRepository;
import com.revature.jobpostservice.repository.JobRepository;
import com.revature.jobpostservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppliedJobsService {

    private final AppliedJobsRepository appliedJobsRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    @Autowired
    public AppliedJobsService(AppliedJobsRepository appliedJobsRepository,JobRepository jobRepository,UserRepository userRepository) {
        this.appliedJobsRepository = appliedJobsRepository;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    // Get all applied jobs
    public List<AppliedJobs> getAllAppliedJobs() {
        return appliedJobsRepository.findAll();
    }

    public AppliedJobs applyToJob(Long jobId, Long jobSeekerId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + jobId));
        User user = userRepository.findById(jobSeekerId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + jobSeekerId));

        AppliedJobs appliedJob = new AppliedJobs();
        appliedJob.setJob(job);
        appliedJob.setUser(user);

        return appliedJobsRepository.save(appliedJob);
    }

    // Get applied job by ID
    public Optional<AppliedJobs> getAppliedJobById(Long id) {
        return appliedJobsRepository.findById(id);
    }

    // Add new applied job
    public AppliedJobs addAppliedJob(AppliedJobs appliedJobs) {
        return appliedJobsRepository.save(appliedJobs);
    }

    // Update an existing applied job
    public AppliedJobs updateAppliedJob(Long id, AppliedJobs appliedJobs) {
        Optional<AppliedJobs> existingAppliedJob = appliedJobsRepository.findById(id);
        if (existingAppliedJob.isPresent()) {
            appliedJobs.setAppliedJobId(id);
            return appliedJobsRepository.save(appliedJobs);
        } else {
            throw new RuntimeException("Applied Job not found with id: " + id);
        }
    }

    // Delete applied job by ID
    public void deleteAppliedJob(Long id) {
        appliedJobsRepository.deleteById(id);
    }
//    public List<Job> getJobsAppliedByUser(Long jobSeekerId) {
//        return appliedJobsRepository.findAllByUser_Id(jobSeekerId)
//                .stream()
//                .map(AppliedJobs::getJob)
//                .collect(Collectors.toList());
//    }

    public List<AppliedJobs> getAllApplicationsForUser(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return appliedJobsRepository.findByUser(user);
    }

}

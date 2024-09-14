package com.revature.jobpostservice.controller;

import com.revature.jobpostservice.model.AppliedJobs;
import com.revature.jobpostservice.model.Job;
import com.revature.jobpostservice.model.User;
import com.revature.jobpostservice.service.AppliedJobsService;
import com.revature.jobpostservice.service.JobService;
import com.revature.jobpostservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/appliedJobs")
public class AppliedJobsController {

    private final AppliedJobsService appliedJobsService;
    private final JobService jobService;
    private final UserService userService;

    @Autowired
    public AppliedJobsController(AppliedJobsService appliedJobsService,JobService jobService,UserService userService) {
        this.appliedJobsService = appliedJobsService;
        this.jobService = jobService;
        this.userService = userService;
    }

    // Get all applied jobs
    @GetMapping
    public List<AppliedJobs> getAllAppliedJobs() {
        return appliedJobsService.getAllAppliedJobs();
    }

    // Get applied job by ID
    @GetMapping("/{id}")
    public ResponseEntity<AppliedJobs> getAppliedJobById(@PathVariable Long id) {
        Optional<AppliedJobs> appliedJob = appliedJobsService.getAppliedJobById(id);
        return appliedJob.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Add a new applied job
    @PostMapping
    public AppliedJobs addAppliedJob(@RequestBody AppliedJobs appliedJobs) {
        return appliedJobsService.addAppliedJob(appliedJobs);
    }


    // Update an existing applied job
    @PutMapping("/{id}")
    public ResponseEntity<AppliedJobs> updateAppliedJob(@PathVariable Long id, @RequestBody AppliedJobs appliedJobs) {
        try {
            AppliedJobs updatedAppliedJob = appliedJobsService.updateAppliedJob(id, appliedJobs);
            return ResponseEntity.ok(updatedAppliedJob);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete applied job by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppliedJob(@PathVariable Long id) {
        appliedJobsService.deleteAppliedJob(id);
        return ResponseEntity.noContent().build();
    }



    @PostMapping("/apply")
    public ResponseEntity<?> applyToJob(
            @RequestParam Long jobId,
            @RequestParam Long jobSeekerId) {

        Job job = jobService.getJobById1(jobId);
        User user = userService.getUserById(jobSeekerId);

        if (user.getExperience().ordinal() < job.getExperienceRequired().ordinal()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Job seeker does not meet the required experience.");
        }

        String[] jobYearRange = job.getYearOfPassing().split("-");
        int startYear = Integer.parseInt(jobYearRange[0]);
        int endYear = Integer.parseInt(jobYearRange[1]);

        if (user.getYearOfPassing() < startYear || user.getYearOfPassing() > endYear) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Job seeker does not meet the required year of passing.");
        }

        List<String> requiredSkills = Arrays.asList(job.getSkillsRequired().split(","));
        List<String> userSkills = Arrays.asList(user.getSkills().split(","));

        if (!userSkills.containsAll(requiredSkills)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Job seeker does not have all the required skills.");
        }

        AppliedJobs appliedJob = appliedJobsService.applyToJob(jobId, jobSeekerId);
        return ResponseEntity.ok(appliedJob);
    }
    @GetMapping("/user/{userId}")
    public List<AppliedJobs> getAllApplicationsForUser(@PathVariable long userId) {
        return appliedJobsService.getAllApplicationsForUser(userId);
    }

}

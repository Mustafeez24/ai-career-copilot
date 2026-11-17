package com.aicareercopilot.jobs;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/jobs")
public class JobsController {
    private final JobsService jobsService;

    public JobsController(JobsService jobsService) {
        this.jobsService = jobsService;
    }

    @GetMapping
    public Map<String, Object> search(
        @RequestParam(name = "q", required = false) String role,
        @RequestParam(required = false) String location,
        @RequestParam(required = false) String experience,
        @RequestParam(required = false) String skills
    ) {
        return jobsService.search(role, location, experience, skills);
    }
}
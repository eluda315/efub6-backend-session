package com.practice.efubaccount.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// @RestController
@RequiredArgsConstructor
public class NewsletterBatchController {

    private final JobLauncher jobLauncher;
    private final Job newsletterJob;

    @GetMapping("/batch/newsletter")
    public String run(@RequestParam String date) throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addString("date", date)
                .toJobParameters();

        JobExecution execution = jobLauncher.run(newsletterJob, params);
        return "결과: " + execution.getStatus();
    }
}
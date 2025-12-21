package org.example.web_service_v2.domain.job_post.exception;

public class JobPostNotFoundException extends RuntimeException {
    public JobPostNotFoundException(String message) {
        super(message);
    }
}

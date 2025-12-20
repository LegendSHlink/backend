package org.example.web_service_v2.domain.job_post.exception;

public class JobPostForbiddenException extends RuntimeException {
    public JobPostForbiddenException(String message) {
        super(message);
    }
}

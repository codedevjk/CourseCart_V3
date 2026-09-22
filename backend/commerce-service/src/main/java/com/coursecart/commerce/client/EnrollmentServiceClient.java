package com.coursecart.commerce.client;

import com.coursecart.commerce.dto.EnrollmentCreateRequest;
import com.coursecart.commerce.dto.EnrollmentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Component
public class EnrollmentServiceClient {

    private final RestTemplate restTemplate;
    
    private static final String ENROLLMENT_SERVICE_CHECK_URL = "http://enrollment-service/api/enrollments/internal/check";
    private static final String ENROLLMENT_SERVICE_CREATE_URL = "http://enrollment-service/api/enrollments/internal/create";

    @Autowired
    public EnrollmentServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @CircuitBreaker(name = "enrollmentService", fallbackMethod = "isUserEnrolledFallback")
    public boolean checkEnrollment(Long userId, Long courseId) {
        try {
            String url = String.format("%s?userId=%d&courseId=%d", ENROLLMENT_SERVICE_CHECK_URL, userId, courseId);
            Boolean isEnrolled = restTemplate.getForObject(url, Boolean.class);
            return isEnrolled != null && isEnrolled;
        } catch (HttpClientErrorException e) {
            return false;
        }
    }

    @CircuitBreaker(name = "enrollmentService", fallbackMethod = "createEnrollmentFallback")
    public EnrollmentDTO createEnrollment(EnrollmentCreateRequest request) {
        return restTemplate.postForObject(ENROLLMENT_SERVICE_CREATE_URL, request, EnrollmentDTO.class);
    }

    public boolean isUserEnrolledFallback(Long userId, Long courseId, Throwable t) {
        throw new RuntimeException("Enrollment service is unavailable.");
    }

    public EnrollmentDTO createEnrollmentFallback(EnrollmentCreateRequest request, Throwable t) {
        throw new RuntimeException("Failed to process enrollment. Please try again.");
    }
}
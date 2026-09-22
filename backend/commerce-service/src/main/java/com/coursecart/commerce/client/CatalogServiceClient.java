package com.coursecart.commerce.client;

import com.coursecart.commerce.dto.CourseDTO;
import com.coursecart.commerce.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Component
public class CatalogServiceClient {

    private final RestTemplate restTemplate;
    
    private static final String CATALOG_SERVICE_URL = "http://catalog-service/api/catalog/courses/";

    @Autowired
    public CatalogServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @CircuitBreaker(name = "catalogService", fallbackMethod = "getCourseFallback")
    public CourseDTO getCourseById(Long courseId) {
        try {
            return restTemplate.getForObject(CATALOG_SERVICE_URL + courseId, CourseDTO.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResourceNotFoundException("Course not found with id: " + courseId);
        }
    }

    public CourseDTO getCourseFallback(Long courseId, Throwable t) {
        if (t instanceof ResourceNotFoundException) {
            throw (ResourceNotFoundException) t;
        }
        throw new RuntimeException("Catalog service is currently unavailable. Please try again later.");
    }
}
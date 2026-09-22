package com.coursecart.commerce.service;

import com.coursecart.commerce.client.CatalogServiceClient;
import com.coursecart.commerce.client.EnrollmentServiceClient;
import com.coursecart.commerce.dto.CheckoutRequest;
import com.coursecart.commerce.dto.CheckoutResponse;
import com.coursecart.commerce.dto.CourseDTO;
import com.coursecart.commerce.dto.EnrollmentCreateRequest;
import com.coursecart.commerce.dto.OrderDTO;
import com.coursecart.commerce.entity.Order;
import com.coursecart.commerce.exception.BadRequestException;
import com.coursecart.commerce.exception.ConflictException;
import com.coursecart.commerce.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommerceServiceImpl implements CommerceService {

    private final OrderRepository orderRepository;
    private final CatalogServiceClient catalogServiceClient;
    private final EnrollmentServiceClient enrollmentServiceClient;

    @Autowired
    public CommerceServiceImpl(OrderRepository orderRepository,
                               CatalogServiceClient catalogServiceClient,
                               EnrollmentServiceClient enrollmentServiceClient) {
        this.orderRepository = orderRepository;
        this.catalogServiceClient = catalogServiceClient;
        this.enrollmentServiceClient = enrollmentServiceClient;
    }

    @Override
    @Transactional
    public CheckoutResponse processCheckout(CheckoutRequest request) {
        // 1. Get Course from Catalog (throws ResourceNotFound if not found)
        CourseDTO course = catalogServiceClient.getCourseById(request.getCourseId());

        // Verify course is ACTIVE
        if (!"ACTIVE".equals(course.getStatus())) {
            throw new BadRequestException("Cannot purchase inactive course");
        }

        // 2. Check Enrollment
        boolean isEnrolled = enrollmentServiceClient.checkEnrollment(request.getUserId(), request.getCourseId());
        if (isEnrolled) {
            throw new ConflictException("User is already enrolled in this course");
        }

        // 3. Mock Payment 
        // We simulate payment success as long as we reach this point.
        if (request.getPaymentMethod() == null || request.getPaymentMethod().trim().isEmpty()) {
             throw new BadRequestException("Payment method is required");
        }

        // 4. Save Order
        Order order = new Order(request.getUserId(), request.getCourseId(), course.getPrice(), request.getPaymentMethod());
        Order savedOrder = orderRepository.save(order);

        // 5. Create Enrollment
        EnrollmentCreateRequest enrollmentReq = new EnrollmentCreateRequest(request.getUserId(), request.getCourseId());
        enrollmentServiceClient.createEnrollment(enrollmentReq);

        return new CheckoutResponse(savedOrder.getId(), "SUCCESS", "Enrolled");
    }

    @Override
    public List<OrderDTO> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> getRecentOrders(int limit) {
        return orderRepository.findAll(PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "orderDate")))
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<OrderDTO> getAllOrders(Pageable pageable) {
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "orderDate"));
        return orderRepository.findAll(sortedPageable).map(this::convertToDTO);
    }

    @Override
    public BigDecimal calculateTotalRevenue() {
        BigDecimal revenue = orderRepository.calculateTotalRevenue();
        return revenue != null ? revenue : BigDecimal.ZERO;
    }

    private OrderDTO convertToDTO(Order order) {
        return new OrderDTO(
                order.getId(),
                order.getUserId(),
                order.getCourseId(),
                order.getAmountPaid(),
                order.getOrderDate(),
                order.getPaymentMethod()
        );
    }
}




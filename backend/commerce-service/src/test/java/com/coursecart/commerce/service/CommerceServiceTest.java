package com.coursecart.commerce.service;

import com.coursecart.commerce.client.CatalogServiceClient;
import com.coursecart.commerce.client.EnrollmentServiceClient;
import com.coursecart.commerce.dto.CheckoutRequest;
import com.coursecart.commerce.dto.CheckoutResponse;
import com.coursecart.commerce.dto.CourseDTO;
import com.coursecart.commerce.dto.EnrollmentCreateRequest;
import com.coursecart.commerce.dto.EnrollmentDTO;
import com.coursecart.commerce.dto.OrderDTO;
import com.coursecart.commerce.entity.Order;
import com.coursecart.commerce.exception.BadRequestException;
import com.coursecart.commerce.exception.ConflictException;
import com.coursecart.commerce.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommerceServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CatalogServiceClient catalogServiceClient;

    @Mock
    private EnrollmentServiceClient enrollmentServiceClient;

    @InjectMocks
    private CommerceServiceImpl commerceService;

    private CourseDTO activeCourse;
    private Order savedOrder;

    @BeforeEach
    void setUp() {
        activeCourse = new CourseDTO(10L, "Java 101", new BigDecimal("19.99"), "ACTIVE");

        savedOrder = new Order(1L, 10L, new BigDecimal("19.99"), "Credit Card");
        savedOrder.setId(50L);
    }

    // --- processCheckout ---

    @Test
    void testProcessCheckout_Success_ActiveCourse_NotEnrolled_ValidPayment() {
        CheckoutRequest request = new CheckoutRequest(1L, 10L, "CARD");

        when(catalogServiceClient.getCourseById(10L)).thenReturn(activeCourse);
        when(enrollmentServiceClient.checkEnrollment(1L, 10L)).thenReturn(false);
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);
        when(enrollmentServiceClient.createEnrollment(any(EnrollmentCreateRequest.class))).thenReturn(new EnrollmentDTO());

        CheckoutResponse response = commerceService.processCheckout(request);

        assertNotNull(response);
        assertEquals("SUCCESS", response.getStatus());
        assertEquals(50L, response.getOrderId());
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(enrollmentServiceClient, times(1)).createEnrollment(any(EnrollmentCreateRequest.class));
    }

    @Test
    void testProcessCheckout_InactiveCourse_ThrowsBadRequestException() {
        CheckoutRequest request = new CheckoutRequest(1L, 10L, "CARD");
        CourseDTO inactiveCourse = new CourseDTO(10L, "Java 101", new BigDecimal("19.99"), "INACTIVE");

        when(catalogServiceClient.getCourseById(10L)).thenReturn(inactiveCourse);

        assertThrows(BadRequestException.class, () -> commerceService.processCheckout(request));

        verify(orderRepository, never()).save(any());
        verify(enrollmentServiceClient, never()).createEnrollment(any());
    }

    @Test
    void testProcessCheckout_DraftCourse_ThrowsBadRequestException() {
        CheckoutRequest request = new CheckoutRequest(1L, 10L, "UPI");
        CourseDTO draftCourse = new CourseDTO(10L, "Java 101", new BigDecimal("19.99"), "DRAFT");

        when(catalogServiceClient.getCourseById(10L)).thenReturn(draftCourse);

        assertThrows(BadRequestException.class, () -> commerceService.processCheckout(request));

        verify(orderRepository, never()).save(any());
        verify(enrollmentServiceClient, never()).createEnrollment(any());
    }

    @Test
    void testProcessCheckout_AlreadyEnrolled_ThrowsConflictException() {
        CheckoutRequest request = new CheckoutRequest(1L, 10L, "CARD");

        when(catalogServiceClient.getCourseById(10L)).thenReturn(activeCourse);
        when(enrollmentServiceClient.checkEnrollment(1L, 10L)).thenReturn(true);

        assertThrows(ConflictException.class, () -> commerceService.processCheckout(request));

        verify(orderRepository, never()).save(any());
        verify(enrollmentServiceClient, never()).createEnrollment(any());
    }

    @Test
    void testProcessCheckout_NullPaymentMethod_ThrowsBadRequestException() {
        // The only mock payment "failure" path: null paymentMethod → BadRequestException before any DB write
        CheckoutRequest request = new CheckoutRequest(1L, 10L, null);

        when(catalogServiceClient.getCourseById(10L)).thenReturn(activeCourse);
        when(enrollmentServiceClient.checkEnrollment(1L, 10L)).thenReturn(false);

        assertThrows(BadRequestException.class, () -> commerceService.processCheckout(request));

        verify(orderRepository, never()).save(any());
        verify(enrollmentServiceClient, never()).createEnrollment(any());
    }

    @Test
    void testProcessCheckout_BlankPaymentMethod_ThrowsBadRequestException() {
        // Blank paymentMethod is also rejected before any DB write
        CheckoutRequest request = new CheckoutRequest(1L, 10L, "   ");

        when(catalogServiceClient.getCourseById(10L)).thenReturn(activeCourse);
        when(enrollmentServiceClient.checkEnrollment(1L, 10L)).thenReturn(false);

        assertThrows(BadRequestException.class, () -> commerceService.processCheckout(request));

        verify(orderRepository, never()).save(any());
        verify(enrollmentServiceClient, never()).createEnrollment(any());
    }

    // --- getOrdersByUserId ---

    @Test
    void testGetOrdersByUserId_ReturnsMappedOrderDTOs() {
        Order order = new Order(1L, 10L, new BigDecimal("19.99"), "Credit Card");
        order.setId(50L);
        when(orderRepository.findByUserId(1L)).thenReturn(Collections.singletonList(order));

        List<OrderDTO> result = commerceService.getOrdersByUserId(1L);

        assertEquals(1, result.size());
        assertEquals(50L, result.get(0).getId());
        assertEquals(new BigDecimal("19.99"), result.get(0).getAmountPaid());
    }

    // --- calculateTotalRevenue ---

    @Test
    void testCalculateTotalRevenue_ReturnsSum() {
        when(orderRepository.calculateTotalRevenue()).thenReturn(new BigDecimal("199.95"));

        BigDecimal revenue = commerceService.calculateTotalRevenue();

        assertEquals(new BigDecimal("199.95"), revenue);
    }

    @Test
    void testCalculateTotalRevenue_NoOrders_ReturnsZero() {
        // Repository returns null when no orders exist → service returns BigDecimal.ZERO
        when(orderRepository.calculateTotalRevenue()).thenReturn(null);

        BigDecimal revenue = commerceService.calculateTotalRevenue();

        assertEquals(BigDecimal.ZERO, revenue);
    }
}



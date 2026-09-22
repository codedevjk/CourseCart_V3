package com.coursecart.commerce.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class OrderDTO {
    private Long id;
    private Long userId;
    private Long courseId;
    private BigDecimal amountPaid;
    private Timestamp orderDate;
    private String paymentMethod;

    public OrderDTO() {
    }

    public OrderDTO(Long id, Long userId, Long courseId, BigDecimal amountPaid, Timestamp orderDate, String paymentMethod) {
        this.id = id;
        this.userId = userId;
        this.courseId = courseId;
        this.amountPaid = amountPaid;
        this.orderDate = orderDate;
        this.paymentMethod = paymentMethod;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public BigDecimal getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(BigDecimal amountPaid) {
        this.amountPaid = amountPaid;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}


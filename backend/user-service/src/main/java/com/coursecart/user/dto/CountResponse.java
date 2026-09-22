package com.coursecart.user.dto;

public class CountResponse {
    
    private long totalUsers;
    
    public CountResponse() {}
    
    public CountResponse(long totalUsers) {
        this.totalUsers = totalUsers;
    }
    
    public long getTotalUsers() {
        return totalUsers;
    }
    
    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }
}

package com.coursecart.user.dto;

import com.coursecart.user.entity.Role;
import com.coursecart.user.entity.User;

public class UserResponse {
    
    private Long id;
    private String name;
    private String username;
    private String role;
    
    public UserResponse() {}
    
    public UserResponse(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.username = user.getUsername();
        this.role = user.getRole().name();
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}

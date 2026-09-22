package com.coursecart.user.service;

import com.coursecart.user.dto.CountResponse;
import com.coursecart.user.dto.UserLoginRequest;
import com.coursecart.user.dto.UserRegistrationRequest;
import com.coursecart.user.dto.UserResponse;
import com.coursecart.user.entity.Role;
import com.coursecart.user.entity.User;
import com.coursecart.user.exception.DuplicateResourceException;
import com.coursecart.user.exception.InvalidCredentialsException;
import com.coursecart.user.exception.ResourceNotFoundException;
import com.coursecart.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse register(UserRegistrationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Username already exists");
        }
        
        User user = new User(
            request.getName(), 
            request.getUsername(), 
            request.getPassword(), 
            Role.USER
        );
        
        user = userRepository.save(user);
        return new UserResponse(user);
    }

    public UserResponse login(UserLoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));
                
        // Plain-string comparison for this educational capstone
        if (!user.getPassword().equals(request.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }
        
        return new UserResponse(user);
    }

    public UserResponse getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return new UserResponse(user);
    }

    public CountResponse countUsers() {
        return new CountResponse(userRepository.count());
    }
}

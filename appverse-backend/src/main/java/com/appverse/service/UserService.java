package com.appverse.service;

import com.appverse.dto.*;
import com.appverse.entity.User;

import java.util.List;

public interface UserService {
    UserDTO registerUser(RegisterRequest request);
    LoginResponse loginUser(LoginRequest request);
    UserDTO getUserById(Long id);
    UserDTO getUserByUsername(String username);
    List<UserDTO> getAllUsers();
    List<UserDTO> getUsersByRole(String role);
    void deleteUser(Long id);
    User findByUsername(String username);
}

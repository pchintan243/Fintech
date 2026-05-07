package com.fintech.backend.service;

import com.fintech.backend.dto.AuthRequestDTO;
import com.fintech.backend.dto.AuthResponseDTO;
import com.fintech.backend.dto.CreateUserRequestDTO;
import com.fintech.backend.dto.RegisterRequestDTO;
import com.fintech.backend.entity.User;

import java.util.Optional;

public interface UserService {
    AuthResponseDTO authenticate(AuthRequestDTO request);
    AuthResponseDTO register(RegisterRequestDTO request);
    Optional<User> getUserByEmail(String email);
    User getCurrentUser(String email);
    java.util.List<User> getAllUsers();
    User createUser(CreateUserRequestDTO request);
    User updateUser(Long id, com.fintech.backend.dto.UpdateUserRequestDTO request);
}

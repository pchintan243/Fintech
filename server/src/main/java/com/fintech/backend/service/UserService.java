package com.fintech.backend.service;

import com.fintech.backend.dto.*;
import com.fintech.backend.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    AuthResponseDTO authenticate(AuthRequestDTO request);
    AuthResponseDTO register(RegisterRequestDTO request);
    Optional<User> getUserByEmail(String email);
    User getCurrentUser(String email);
    CreateUserResponseDTO createUser(CreateUserRequestDTO request);
    UpdateUserResponseDTO updateUser(Long id, UpdateUserRequestDTO request);
    void deleteUser(Long id);

    // Projection-based read methods (no entity serialization)
    List<UserProjection> getAllUsersProjected();
    Optional<UserProjection> getUserByIdProjected(Long id);
    Optional<UserProjection> getUserByEmailProjected(String email);
}

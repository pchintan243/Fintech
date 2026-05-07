package com.fintech.backend.controller;

import com.fintech.backend.dto.CreateUserRequestDTO;
import com.fintech.backend.dto.CreateUserResponseDTO;
import com.fintech.backend.dto.UpdateUserRequestDTO;
import com.fintech.backend.dto.UpdateUserResponseDTO;
import com.fintech.backend.dto.UserProjection;
import com.fintech.backend.entity.User;
import com.fintech.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private User getAuthenticatedUser(Authentication authentication) {
        if (authentication == null || authentication.getName().equals("anonymousUser")) {
            throw new RuntimeException("Authentication required");
        }
        return userService.getCurrentUser(authentication.getName());
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserProjection>> getAllUsers(Authentication authentication) {
        return ResponseEntity.ok(userService.getAllUsersProjected());
    }

    @GetMapping("/me")
    public ResponseEntity<UserProjection> getCurrentUser(Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        return userService.getUserByIdProjected(user.getId())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserProjection> getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmailProjected(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CreateUserResponseDTO> createUser(@RequestBody CreateUserRequestDTO dto) {
        return ResponseEntity.ok(userService.createUser(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UpdateUserResponseDTO> updateUser(@PathVariable Long id, @RequestBody UpdateUserRequestDTO dto) {
        return ResponseEntity.ok(userService.updateUser(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}

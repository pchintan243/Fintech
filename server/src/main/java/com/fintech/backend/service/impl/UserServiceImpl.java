package com.fintech.backend.service.impl;

import com.fintech.backend.dto.*;
import com.fintech.backend.entity.User;
import com.fintech.backend.enums.AccountTier;
import com.fintech.backend.enums.UserKycStatus;
import com.fintech.backend.enums.UserRole;
import com.fintech.backend.repository.UserRepository;
import com.fintech.backend.service.EmailService;
import com.fintech.backend.service.UserService;
import com.fintech.backend.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final EmailService emailService;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
                           JwtUtil jwtUtil, AuthenticationManager authenticationManager,
                           UserDetailsService userDetailsService, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.emailService = emailService;
    }

    @Override
    public AuthResponseDTO authenticate(AuthRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();

        return AuthResponseDTO.builder()
                .token(jwt)
                .email(user.getEmail())
                .id(user.getId())
                .fullName(user.getFullName())
                .role(user.getRole().name())
                .build();
    }

    @Override
    public AuthResponseDTO register(RegisterRequestDTO request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .email(request.getEmail())
                .fullName(request.getFullName())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .role(UserRole.ROLE_USER)
                .build();

        userRepository.save(user);

        final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);

        return AuthResponseDTO.builder()
                .token(jwt)
                .email(user.getEmail())
                .id(user.getId())
                .fullName(user.getFullName())
                .build();
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User getCurrentUser(String email) {
        return userRepository.findById(Long.parseLong(email))
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
    }

    @Override
    public List<UserProjection> getAllUsersProjected() {
        return userRepository.findAllProjectedBy();
    }

    @Override
    public Optional<UserProjection> getUserByIdProjected(Long id) {
        return userRepository.findProjectedById(id);
    }

    @Override
    public Optional<UserProjection> getUserByEmailProjected(String email) {
        return userRepository.findProjectedByEmail(email);
    }

    @Override
    public CreateUserResponseDTO createUser(CreateUserRequestDTO request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        String rawPassword = "Fintech" + System.currentTimeMillis() % 10000 + "!";
        User user = User.builder()
                .email(request.getEmail())
                .fullName(request.getFullName())
                .password(passwordEncoder.encode(rawPassword))
                .phone(request.getPhone())
                .country(request.getCountry())
                .role(UserRole.ROLE_USER)
                .build();

        User saved = userRepository.save(user);
        emailService.sendWelcomeEmail(saved, rawPassword);

        return CreateUserResponseDTO.builder()
                .id(saved.getId())
                .email(saved.getEmail())
                .fullName(saved.getFullName())
                .role(saved.getRole().name())
                .build();
    }

    @Override
    public UpdateUserResponseDTO updateUser(Long id, UpdateUserRequestDTO request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));

        if (request.getKycStatus() != null) {
            user.setKycStatus(UserKycStatus.valueOf(request.getKycStatus()));
        }
        if (request.getAccountTier() != null) {
            user.setAccountTier(AccountTier.valueOf(request.getAccountTier()));
        }

        User saved = userRepository.save(user);

        return UpdateUserResponseDTO.builder()
                .id(saved.getId())
                .email(saved.getEmail())
                .fullName(saved.getFullName())
                .kycStatus(saved.getKycStatus().name())
                .accountTier(saved.getAccountTier().name())
                .role(saved.getRole().name())
                .build();
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found: " + id);
        }
        userRepository.deleteById(id);
    }
}

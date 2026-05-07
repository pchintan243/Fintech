package com.fintech.backend.repository;

import com.fintech.backend.dto.UserProjection;
import com.fintech.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    List<UserProjection> findAllProjectedBy();

    Optional<UserProjection> findProjectedById(Long id);

    Optional<UserProjection> findProjectedByEmail(String email);
}

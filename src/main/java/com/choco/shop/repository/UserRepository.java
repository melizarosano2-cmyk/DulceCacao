package com.choco.shop.repository;

import com.choco.shop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    List<User> findByRoles_Name(String roleName);

    Optional<User> findByEmail(String email);

    Optional<User> findByVerificationToken(String token);

    Optional<User> findByResetToken(String token);
}

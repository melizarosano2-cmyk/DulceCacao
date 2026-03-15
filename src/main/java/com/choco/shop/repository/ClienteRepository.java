package com.choco.shop.repository;

import com.choco.shop.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByUsername(String username);

    Optional<Cliente> findByEmail(String email);
}

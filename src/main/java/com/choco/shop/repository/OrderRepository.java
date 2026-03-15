package com.choco.shop.repository;

import com.choco.shop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByClienteId(Long clienteId);
}

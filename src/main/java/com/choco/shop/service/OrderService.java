package com.choco.shop.service;

import com.choco.shop.entity.Order;
import com.choco.shop.entity.Cliente;
import java.util.List;

public interface OrderService {
    List<Order> findAll();

    List<Order> findByUser(Cliente cliente);

    Order findById(Long id);

    Order save(Order order);

    void deleteById(Long id);

    Order createOrder(Cliente cliente, java.util.Map<Long, Integer> cart);
}

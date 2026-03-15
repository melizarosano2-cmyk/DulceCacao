package com.choco.shop.service.impl;

import com.choco.shop.entity.Order;
import com.choco.shop.entity.OrderItem;
import com.choco.shop.entity.Product;
import com.choco.shop.entity.Cliente;
import com.choco.shop.repository.OrderRepository;
import com.choco.shop.repository.ProductRepository;
import com.choco.shop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> findByUser(Cliente cliente) {
        return orderRepository.findByClienteId(cliente.getId());
    }

    @Override
    public Order findById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Order createOrder(Cliente cliente, java.util.Map<Long, Integer> cart) {
        System.out.println("DEBUG: Inside createOrder");
        Order order = new Order();
        order.setCliente(cliente);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("Pendiente");

        List<OrderItem> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        System.out.println("DEBUG: Processing cart items. Size: " + cart.size());
        for (java.util.Map.Entry<Long, Integer> entry : cart.entrySet()) {
            Long productId = entry.getKey();
            Integer quantity = entry.getValue();
            System.out.println("DEBUG: Processing product ID: " + productId);

            Product product = productRepository.findById(productId).orElse(null);
            if (product != null) {
                // Actualizar el stock del producto
                int currentStock = product.getStock() != null ? product.getStock() : 0;
                int newStock = currentStock - quantity;
                product.setStock(Math.max(0, newStock)); // Evitar stock negativo por seguridad
                productRepository.save(product);

                OrderItem item = new OrderItem();
                item.setOrder(order);
                item.setProduct(product);
                item.setQuantity(quantity);
                item.setPrice(product.getPrice());
                items.add(item);

                BigDecimal itemTotal = product.getPrice().multiply(new BigDecimal(quantity));
                total = total.add(itemTotal);
            } else {
                System.out.println("DEBUG: Product not found for ID: " + productId);
            }
        }

        order.setItems(items);
        order.setTotal(total);

        System.out.println("DEBUG: Saving order. Items count: " + items.size() + ", Total: " + total);
        return orderRepository.save(order);
    }
}

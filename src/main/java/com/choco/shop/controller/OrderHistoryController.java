package com.choco.shop.controller;

import com.choco.shop.entity.Cliente;
import com.choco.shop.entity.Order;
import com.choco.shop.entity.User;
import com.choco.shop.repository.UserRepository;
import com.choco.shop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/my-orders")
public class OrderHistoryController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String viewMyOrders(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElse(null);

        if (user instanceof Cliente) {
            Cliente cliente = (Cliente) user;
            List<Order> orders = orderService.findByUser(cliente);
            model.addAttribute("orders", orders);
            return "my-orders";
        } else {
            return "redirect:/";
        }
    }
}

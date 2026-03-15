package com.choco.shop.controller;

import com.choco.shop.entity.Product;
import com.choco.shop.repository.OrderRepository;
import com.choco.shop.service.CategoryService;
import com.choco.shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Today's metrics
        long todayOrders = 8; // Simplified - would filter by today's date
        long pendingOrders = orderRepository.findAll().stream()
                .filter(o -> "Pendiente".equals(o.getStatus())).count();

        // Low stock products
        java.util.List<Product> lowStockProducts = productService.findAll().stream()
                .filter(p -> p.getStock() < 20)
                .sorted((a, b) -> Integer.compare(a.getStock(), b.getStock()))
                .limit(10)
                .collect(java.util.stream.Collectors.toList());
        long lowStockCount = lowStockProducts.size();

        // Today's sales (simplified)
        int todaySales = 2450;

        // Recent orders
        java.util.List<com.choco.shop.entity.Order> recentOrders = orderRepository.findAll().stream()
                .sorted((a, b) -> b.getOrderDate().compareTo(a.getOrderDate()))
                .limit(10)
                .collect(java.util.stream.Collectors.toList());

        // Order status data for chart
        java.util.List<Long> orderStatusData = java.util.Arrays.asList(
                orderRepository.findAll().stream().filter(o -> "Entregado".equals(o.getStatus())).count(),
                pendingOrders);

        model.addAttribute("todayOrders", todayOrders);
        model.addAttribute("pendingOrders", pendingOrders);
        model.addAttribute("lowStockCount", lowStockCount);
        model.addAttribute("todaySales", todaySales);
        model.addAttribute("lowStockProducts", lowStockProducts);
        model.addAttribute("recentOrders", recentOrders);
        model.addAttribute("orderStatusData", orderStatusData);

        return "employee/dashboard";
    }

    // Product Management
    @GetMapping("/products")
    public String products(Model model) {
        model.addAttribute("products", productService.findAll());
        return "employee/products";
    }

    @GetMapping("/products/new")
    public String newProduct(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.findAll());
        return "employee/product_form";
    }

    @GetMapping("/products/edit/{id}")
    public String editProduct(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.findById(id).orElse(new Product()));
        model.addAttribute("categories", categoryService.findAll());
        return "employee/product_form";
    }

    @PostMapping("/products")
    public String saveProduct(@ModelAttribute Product product) {
        productService.save(product);
        return "redirect:/employee/products";
    }

    // Order Management
    @GetMapping("/orders")
    public String orders(Model model) {
        model.addAttribute("orders", orderRepository.findAll());
        return "employee/orders";
    }

    @PostMapping("/orders/{id}/status")
    public String updateOrderStatus(@PathVariable Long id, @RequestParam String status) {
        orderRepository.findById(id).ifPresent(order -> {
            order.setStatus(status);
            orderRepository.save(order);
        });
        return "redirect:/employee/orders";
    }
}

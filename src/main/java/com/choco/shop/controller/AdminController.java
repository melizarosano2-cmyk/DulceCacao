package com.choco.shop.controller;

import com.choco.shop.entity.Product;
import com.choco.shop.service.CategoryService;
import com.choco.shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private com.choco.shop.repository.UserRepository userRepository;
    @Autowired
    private com.choco.shop.repository.RoleRepository roleRepository;
    @Autowired
    private com.choco.shop.repository.OrderRepository orderRepository;
    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Basic KPIs
        long totalOrders = orderRepository.count();
        long totalProducts = productService.findAll().size();
        long totalEmployees = userRepository.findByRoles_Name("ROLE_EMPLOYEE").size();

        // Calculate total sales
        java.math.BigDecimal totalSales = orderRepository.findAll().stream()
                .map(order -> order.getTotal())
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);

        // Get low stock products (less than 20 units)
        java.util.List<Product> lowStockProducts = productService.findAll().stream()
                .filter(p -> p.getStock() < 20)
                .sorted((a, b) -> Integer.compare(a.getStock(), b.getStock()))
                .collect(java.util.stream.Collectors.toList());

        // Sales trend data (last 30 days - simplified with mock data for now)
        java.util.List<String> salesDates = java.util.Arrays.asList(
                "Día 1", "Día 5", "Día 10", "Día 15", "Día 20", "Día 25", "Día 30");
        java.util.List<Integer> salesAmounts = java.util.Arrays.asList(
                1200, 1500, 1800, 2100, 1900, 2300, 2500);

        // Order status counts
        long deliveredCount = orderRepository.findAll().stream()
                .filter(o -> "Entregado".equals(o.getStatus())).count();
        long pendingCount = orderRepository.findAll().stream()
                .filter(o -> "Pendiente".equals(o.getStatus())).count();
        long canceledCount = orderRepository.findAll().stream()
                .filter(o -> "Cancelado".equals(o.getStatus())).count();
        java.util.List<Long> orderStatusCounts = java.util.Arrays.asList(
                deliveredCount, pendingCount, canceledCount);

        // Top 5 products by name (simplified - in real app would track actual sales)
        java.util.List<Product> allProducts = productService.findAll();
        java.util.List<String> topProductNames = allProducts.stream()
                .limit(5)
                .map(Product::getName)
                .collect(java.util.stream.Collectors.toList());
        java.util.List<Integer> topProductSales = java.util.Arrays.asList(45, 38, 32, 28, 25);

        // Revenue by category
        java.util.Map<String, java.math.BigDecimal> categoryRevenue = new java.util.HashMap<>();
        allProducts.forEach(product -> {
            String categoryName = product.getCategory().getName();
            java.math.BigDecimal revenue = product.getPrice().multiply(
                    new java.math.BigDecimal(Math.max(0, 100 - product.getStock())));
            categoryRevenue.merge(categoryName, revenue, java.math.BigDecimal::add);
        });

        java.util.List<String> categoryNames = new java.util.ArrayList<>(categoryRevenue.keySet());
        java.util.List<Integer> categoryRevenues = categoryRevenue.values().stream()
                .map(java.math.BigDecimal::intValue)
                .collect(java.util.stream.Collectors.toList());

        // Add all attributes to model
        model.addAttribute("totalSales", totalSales.intValue());
        model.addAttribute("totalOrders", totalOrders);
        model.addAttribute("totalProducts", totalProducts);
        model.addAttribute("totalEmployees", totalEmployees);
        model.addAttribute("lowStockProducts", lowStockProducts);
        model.addAttribute("salesDates", salesDates);
        model.addAttribute("salesAmounts", salesAmounts);
        model.addAttribute("orderStatusCounts", orderStatusCounts);
        model.addAttribute("topProductNames", topProductNames);
        model.addAttribute("topProductSales", topProductSales);
        model.addAttribute("categoryNames", categoryNames);
        model.addAttribute("categoryRevenues", categoryRevenues);

        return "admin/dashboard";
    }

    // Product Management
    @GetMapping("/products")
    public String products(Model model) {
        model.addAttribute("products", productService.findAll());
        return "admin/products";
    }

    @GetMapping("/products/new")
    public String newProduct(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.findAll());
        return "admin/product_form";
    }

    @GetMapping("/products/edit/{id}")
    public String editProduct(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.findById(id).orElse(new Product()));
        model.addAttribute("categories", categoryService.findAll());
        return "admin/product_form";
    }

    @PostMapping("/products")
    public String saveProduct(@ModelAttribute Product product) {
        productService.save(product);
        return "redirect:/admin/products";
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);
        return "redirect:/admin/products";
    }

    // Employee Management
    @GetMapping("/employees")
    public String employees(Model model) {
        model.addAttribute("employees", userRepository.findByRoles_Name("ROLE_EMPLOYEE"));
        return "admin/employees";
    }

    @GetMapping("/employees/new")
    public String newEmployee(Model model) {
        model.addAttribute("employee", new com.choco.shop.entity.User());
        return "admin/employee_form";
    }

    @PostMapping("/employees")
    public String saveEmployee(@ModelAttribute com.choco.shop.entity.User employee) {
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        java.util.Set<com.choco.shop.entity.Role> roles = new java.util.HashSet<>();
        roleRepository.findByName("ROLE_EMPLOYEE").ifPresent(roles::add);
        employee.setRoles(roles);
        userRepository.save(employee);
        return "redirect:/admin/employees";
    }

    @GetMapping("/employees/delete/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "redirect:/admin/employees";
    }
}

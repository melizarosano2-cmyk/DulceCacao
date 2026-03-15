package com.choco.shop.config;

import com.choco.shop.entity.*;
import com.choco.shop.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

@Component
public class DataSeeder implements CommandLineRunner {

        @Autowired
        private RoleRepository roleRepository;
        @Autowired
        private UserRepository userRepository;
        @Autowired
        private CategoryRepository categoryRepository;
        @Autowired
        private ProductRepository productRepository;
        @Autowired
        private OrderRepository orderRepository;
        @Autowired
        private PasswordEncoder passwordEncoder;

        @Override
        public void run(String... args) throws Exception {
                seedRoles();
                seedUsers();
                seedCategoriesAndProducts();
                seedOrders();
        }

        private void seedRoles() {
                if (roleRepository.count() == 0) {
                        Role admin = new Role();
                        admin.setName("ROLE_ADMIN");
                        roleRepository.save(admin);

                        Role employee = new Role();
                        employee.setName("ROLE_EMPLOYEE");
                        roleRepository.save(employee);

                        Role customer = new Role();
                        customer.setName("ROLE_CUSTOMER");
                        roleRepository.save(customer);
                }
        }

        private void seedUsers() {
                if (userRepository.count() == 0) {
                        // Admin
                        User admin = new User();
                        admin.setUsername("admin");
                        admin.setPassword(passwordEncoder.encode("admin123"));
                        admin.setEmail("admin@dulcecacao.com");
                        admin.setFullName("Admin User");
                        admin.setActive(true);
                        admin.setEmailVerified(true);
                        admin.setRoles(new HashSet<>(Arrays.asList(roleRepository.findByName("ROLE_ADMIN").get())));
                        userRepository.save(admin);

                        // Employee
                        Empleado employee = new Empleado();
                        employee.setUsername("employee");
                        employee.setPassword(passwordEncoder.encode("employee123"));
                        employee.setEmail("employee@dulcecacao.com");
                        employee.setFullName("Employee User");
                        employee.setActive(true);
                        employee.setEmailVerified(true);
                        employee.setSueldo(new BigDecimal("2500.00"));
                        employee.setRoles(
                                        new HashSet<>(Arrays.asList(roleRepository.findByName("ROLE_EMPLOYEE").get())));
                        userRepository.save(employee);

                        // Customers
                        for (int i = 1; i <= 5; i++) {
                                Cliente customer = new Cliente();
                                customer.setUsername("customer" + i);
                                customer.setPassword(passwordEncoder.encode("password"));
                                customer.setEmail("customer" + i + "@example.com");
                                customer.setFullName("Customer " + i);
                                customer.setActive(true);
                                customer.setEmailVerified(true);
                                customer.setDireccion("Calle " + i + ", Montevideo");
                                customer.setTelefono("+598 9" + (1000000 + i));
                                customer.setRecibirPromociones(i % 2 == 0 ? "Si" : "No");
                                customer.setRoles(new HashSet<>(
                                                Arrays.asList(roleRepository.findByName("ROLE_CUSTOMER").get())));
                                userRepository.save(customer);
                        }
                }
        }

        private void seedCategoriesAndProducts() {
                if (categoryRepository.count() == 0) {
                        Category bombones = new Category();
                        bombones.setName("Bombones");
                        categoryRepository.save(bombones);

                        Category tabletas = new Category();
                        tabletas.setName("Tabletas");
                        categoryRepository.save(tabletas);

                        Category trufas = new Category();
                        trufas.setName("Trufas");
                        categoryRepository.save(trufas);

                        Category regalos = new Category();
                        regalos.setName("Regalos");
                        categoryRepository.save(regalos);

                        // Products
                        createProduct("Bombón de Avellana", "Delicioso bombón relleno de crema de avellana.",
                                        new BigDecimal("1.50"), 100, bombones, "/images/bonbon.png");
                        createProduct("Bombón de Dulce de Leche", "Clásico relleno de dulce de leche uruguayo.",
                                        new BigDecimal("1.50"), 100, bombones, "/images/bonbon.png");
                        createProduct("Bombón de Maracuyá", "Exótico relleno de maracuyá.", new BigDecimal("1.80"), 80,
                                        bombones, "/images/bonbon.png");
                        createProduct("Bombón de Café", "Intenso sabor a café tostado.", new BigDecimal("1.60"), 90,
                                        bombones, "/images/bonbon.png");
                        createProduct("Bombón de Menta", "Refrescante relleno de menta.", new BigDecimal("1.50"), 100,
                                        bombones, "/images/bonbon.png");

                        createProduct("Tableta 70% Cacao", "Chocolate amargo intenso.", new BigDecimal("5.00"), 50,
                                        tabletas, "/images/bar.png");
                        createProduct("Tableta con Almendras", "Chocolate con leche y almendras enteras.",
                                        new BigDecimal("6.00"), 60, tabletas, "/images/bar.png");
                        createProduct("Tableta Blanca con Oreo", "Chocolate blanco con trozos de galleta.",
                                        new BigDecimal("5.50"), 40, tabletas, "/images/bar.png");
                        createProduct("Tableta Naranja", "Chocolate semi-amargo con cáscara de naranja.",
                                        new BigDecimal("5.80"), 45, tabletas, "/images/bar.png");
                        createProduct("Tableta Sal Marina", "Chocolate con un toque de sal marina.",
                                        new BigDecimal("5.20"), 55, tabletas, "/images/bar.png");

                        createProduct("Trufa de Chocolate Negro", "Trufa clásica bañada en cacao.",
                                        new BigDecimal("2.00"), 70, trufas, "/images/truffle.png");
                        createProduct("Trufa de Coco", "Trufa blanca rebozada en coco.", new BigDecimal("2.00"), 70,
                                        trufas, "/images/truffle.png");
                        createProduct("Trufa de Pistacho", "Trufa cremosa con pistacho.", new BigDecimal("2.50"), 60,
                                        trufas, "/images/truffle.png");
                        createProduct("Trufa de Frambuesa", "Trufa con corazón de frambuesa.", new BigDecimal("2.20"),
                                        65, trufas, "/images/truffle.png");
                        createProduct("Trufa de Champagne", "Elegante trufa con un toque de champagne.",
                                        new BigDecimal("2.80"), 50, trufas, "/images/truffle.png");

                        createProduct("Caja de Regalo Surtida", "Selección de 12 bombones variados.",
                                        new BigDecimal("20.00"), 30, regalos, "/images/bonbon.png");
                        createProduct("Caja Corazón", "Caja especial para enamorados.", new BigDecimal("25.00"), 20,
                                        regalos, "/images/bonbon.png");
                        createProduct("Canasta Dulce", "Mix de tabletas y trufas.", new BigDecimal("40.00"), 10,
                                        regalos, "/images/bar.png");
                        createProduct("Caja Premium", "Selección del chocolatier.", new BigDecimal("50.00"), 5, regalos,
                                        "/images/truffle.png");
                        createProduct("Gift Card $500", "Tarjeta de regalo.", new BigDecimal("500.00"), 100, regalos,
                                        "/images/logo.png");
                }
        }

        private void createProduct(String name, String description, BigDecimal price, Integer stock, Category category,
                        String imageUrl) {
                Product product = new Product();
                product.setName(name);
                product.setDescription(description);
                product.setPrice(price);
                product.setStock(stock);
                product.setCategory(category);
                product.setImageUrl(imageUrl);
                productRepository.save(product);
        }

        private void seedOrders() {
                if (orderRepository.count() == 0) {
                        List<User> users = userRepository.findAll();
                        List<Product> products = productRepository.findAll();
                        Random random = new Random();

                        for (int i = 0; i < 50; i++) {
                                Order order = new Order();
                                User user = users.get(random.nextInt(users.size()));
                                if (user instanceof Cliente && user.getRoles().stream()
                                                .anyMatch(r -> r.getName().equals("ROLE_CUSTOMER"))) {
                                        order.setCliente((Cliente) user);
                                        order.setOrderDate(LocalDateTime.now().minusDays(random.nextInt(30)));
                                        order.setStatus(random.nextBoolean() ? "Entregado" : "Pendiente");

                                        BigDecimal total = BigDecimal.ZERO;
                                        int itemCount = random.nextInt(5) + 1;
                                        for (int j = 0; j < itemCount; j++) {
                                                Product p = products.get(random.nextInt(products.size()));
                                                total = total.add(p.getPrice());
                                        }
                                        order.setTotal(total);
                                        orderRepository.save(order);
                                }
                        }
                }
        }
}

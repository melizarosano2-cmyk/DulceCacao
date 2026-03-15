package com.choco.shop.controller;

import com.choco.shop.entity.Cliente;
import com.choco.shop.entity.Role;
import com.choco.shop.repository.ClienteRepository;
import com.choco.shop.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashSet;
import java.util.Arrays;

@Controller
public class AuthController {
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private com.choco.shop.service.EmailService emailService;

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute Cliente cliente) {
        cliente.setPassword(passwordEncoder.encode(cliente.getPassword()));
        cliente.setActive(true);
        cliente.setEmailVerified(false);

        // Generate verification token
        String verificationToken = java.util.UUID.randomUUID().toString();
        cliente.setVerificationToken(verificationToken);

        Role customerRole = roleRepository.findByName("ROLE_CUSTOMER").orElseGet(() -> {
            throw new RuntimeException("ROLE_CUSTOMER not found");
        });
        cliente.setRoles(new HashSet<>(Arrays.asList(customerRole)));
        clienteRepository.save(cliente);

        // Send verification email
        emailService.sendVerificationEmail(cliente, verificationToken);

        return "redirect:/verification-sent";
    }

    @GetMapping("/verification-sent")
    public String verificationSent() {
        return "verification-sent";
    }
}

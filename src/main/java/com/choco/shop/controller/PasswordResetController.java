package com.choco.shop.controller;

import com.choco.shop.entity.User;
import com.choco.shop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Controller
public class PasswordResetController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private com.choco.shop.service.EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email, RedirectAttributes redirectAttributes) {
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            String token = UUID.randomUUID().toString();
            user.setResetToken(token);
            user.setResetTokenExpiry(LocalDateTime.now().plusHours(1));
            userRepository.save(user);

            emailService.sendPasswordResetEmail(user, token);
            redirectAttributes.addFlashAttribute("message",
                    "Se ha enviado un correo con instrucciones para restablecer tu contraseña.");
        } else {
            redirectAttributes.addFlashAttribute("message",
                    "Si el correo existe en nuestro sistema, recibirás instrucciones para restablecer tu contraseña.");
        }

        return "redirect:/reset-sent";
    }

    @GetMapping("/reset-sent")
    public String showResetSent() {
        return "reset-sent";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        Optional<User> userOpt = userRepository.findByResetToken(token);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getResetTokenExpiry().isAfter(LocalDateTime.now())) {
                model.addAttribute("token", token);
                model.addAttribute("valid", true);
                return "reset-password";
            }
        }

        model.addAttribute("valid", false);
        model.addAttribute("message", "El enlace de restablecimiento es inválido o ha expirado.");
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam("token") String token,
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword,
            RedirectAttributes redirectAttributes) {

        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Las contraseñas no coinciden.");
            return "redirect:/reset-password?token=" + token;
        }

        Optional<User> userOpt = userRepository.findByResetToken(token);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getResetTokenExpiry().isAfter(LocalDateTime.now())) {
                user.setPassword(passwordEncoder.encode(password));
                user.setResetToken(null);
                user.setResetTokenExpiry(null);
                userRepository.save(user);

                redirectAttributes.addFlashAttribute("message",
                        "Tu contraseña ha sido restablecida exitosamente. Ya puedes iniciar sesión.");
                return "redirect:/login";
            }
        }

        redirectAttributes.addFlashAttribute("error", "El enlace de restablecimiento es inválido o ha expirado.");
        return "redirect:/login";
    }
}

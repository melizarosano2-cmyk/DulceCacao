package com.choco.shop.controller;

import com.choco.shop.entity.User;
import com.choco.shop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class VerificationController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam("token") String token, Model model) {
        Optional<User> userOpt = userRepository.findByVerificationToken(token);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setEmailVerified(true);
            user.setVerificationToken(null);
            userRepository.save(user);

            model.addAttribute("success", true);
            model.addAttribute("message",
                    "¡Tu correo electrónico ha sido verificado exitosamente! Ya puedes iniciar sesión.");
        } else {
            model.addAttribute("success", false);
            model.addAttribute("message", "El enlace de verificación es inválido o ha expirado.");
        }

        return "email-verified";
    }
}

package com.choco.shop.controller;

import com.choco.shop.entity.Cliente;
import com.choco.shop.entity.User;
import com.choco.shop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/my-account")
public class AccountController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public String myAccount(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElse(null);

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);

        // Check if user is a Cliente to show additional info
        if (user instanceof Cliente) {
            model.addAttribute("isCliente", true);
            model.addAttribute("cliente", (Cliente) user);
        } else {
            model.addAttribute("isCliente", false);
        }

        return "my-account";
    }

    @org.springframework.web.bind.annotation.PostMapping("/update")
    public String updateAccount(Authentication authentication,
            @org.springframework.web.bind.annotation.RequestParam String fullName,
            @org.springframework.web.bind.annotation.RequestParam String email,
            @org.springframework.web.bind.annotation.RequestParam(required = false) String direccion,
            @org.springframework.web.bind.annotation.RequestParam(required = false) String telefono,
            @org.springframework.web.bind.annotation.RequestParam(required = false) String recibirPromociones,
            org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElse(null);

        if (user == null) {
            return "redirect:/login";
        }

        user.setFullName(fullName);
        user.setEmail(email);

        if (user instanceof Cliente) {
            Cliente cliente = (Cliente) user;
            cliente.setDireccion(direccion);
            cliente.setTelefono(telefono);
            cliente.setRecibirPromociones(recibirPromociones);
        }

        userRepository.save(user);
        redirectAttributes.addFlashAttribute("message", "Perfil actualizado correctamente.");

        return "redirect:/my-account";
    }
}

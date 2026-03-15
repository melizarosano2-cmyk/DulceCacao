package com.choco.shop.controller;

import com.choco.shop.entity.Order;
import com.choco.shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        Map<Long, Integer> cart = getCart(session);
        List<com.choco.shop.dto.CartItemDTO> cartItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
            productService.findById(entry.getKey()).ifPresent(product -> {
                com.choco.shop.dto.CartItemDTO item = new com.choco.shop.dto.CartItemDTO(product, entry.getValue());
                cartItems.add(item);
            });
        }

        for (com.choco.shop.dto.CartItemDTO item : cartItems) {
            total = total.add(item.getSubtotal());
        }

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", total);
        return "cart";
    }

    @GetMapping("/add/{id}")
    public String addToCart(@PathVariable Long id, 
            @RequestParam(defaultValue = "1") int quantity,
            HttpSession session, Authentication authentication,
            RedirectAttributes redirectAttributes) {
        System.out.println("DEBUG: Adding product to cart. ID: " + id + ", Quantity: " + quantity);

        // Check if user is authenticated
        if (authentication == null || !authentication.isAuthenticated() ||
                authentication.getPrincipal().equals("anonymousUser")) {
            System.out.println("DEBUG: User not authenticated, redirecting to register.");
            redirectAttributes.addFlashAttribute("message",
                    "Por favor, inicia sesión o regístrate para agregar productos al carrito.");
            return "redirect:/register";
        }

        Map<Long, Integer> cart = getCart(session);
        cart.put(id, cart.getOrDefault(id, 0) + quantity);
        session.setAttribute("cart", cart);
        System.out.println("DEBUG: Product added. Current cart size: " + cart.size());
        System.out.println("DEBUG: Cart content: " + cart);

        return "redirect:/";
    }

    @GetMapping("/remove/{id}")
    public String removeFromCart(@PathVariable Long id, HttpSession session) {
        Map<Long, Integer> cart = getCart(session);
        cart.remove(id);
        session.setAttribute("cart", cart);
        return "redirect:/cart";
    }

    @GetMapping("/update/{id}")
    public String updateQuantity(@PathVariable Long id, @RequestParam int quantity, HttpSession session) {
        Map<Long, Integer> cart = getCart(session);
        if (quantity > 0) {
            cart.put(id, quantity);
        } else {
            cart.remove(id);
        }
        session.setAttribute("cart", cart);
        return "redirect:/cart";
    }

    @Autowired
    private com.choco.shop.service.OrderService orderService;

    @Autowired
    private com.choco.shop.repository.UserRepository userRepository;

    @PostMapping("/checkout")
    public String checkout(HttpSession session, Authentication authentication, Model model,
            RedirectAttributes redirectAttributes) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        Map<Long, Integer> cart = getCart(session);
        if (cart.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "El carrito está vacío.");
            return "redirect:/cart";
        }

        String username = authentication.getName();
        com.choco.shop.entity.User user = userRepository.findByUsername(username).orElse(null);

        try {
            if (user instanceof com.choco.shop.entity.Cliente) {
                System.out.println("DEBUG: User is instance of Cliente");
                com.choco.shop.entity.Cliente cliente = (com.choco.shop.entity.Cliente) user;
                System.out.println("DEBUG: Calling orderService.createOrder");
                Order order = orderService.createOrder(cliente, cart);
                System.out.println("DEBUG: Order created with ID: " + (order != null ? order.getId() : "null"));

                // Clear the cart
                session.removeAttribute("cart");
                redirectAttributes.addFlashAttribute("message", "¡Pedido realizado con éxito!");
                return "redirect:/";
            } else {
                redirectAttributes.addFlashAttribute("error", "Solo los clientes pueden realizar pedidos.");
                return "redirect:/cart";
            }
        } catch (Exception e) {
            System.err.println("CRITICAL ERROR IN CHECKOUT:");
            e.printStackTrace();
            throw e;
        }
    }

    @SuppressWarnings("unchecked")
    private Map<Long, Integer> getCart(HttpSession session) {
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
            session.setAttribute("cart", cart);
        }
        return cart;
    }
}

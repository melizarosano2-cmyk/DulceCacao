package com.choco.shop.controller.api;

import com.choco.shop.dto.AnalyticsDTO;
import com.choco.shop.entity.Order;
import com.choco.shop.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;

@RestController
@RequestMapping("/api/v1/analytics")
public class DashboardRestController {

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/sales")
    public ResponseEntity<AnalyticsDTO> getSalesAnalytics() {
        // Recalculate logic from AdminController to serve JSON
        List<Order> allOrders = orderRepository.findAll();
        Map<String, BigDecimal> salesByMonth = new LinkedHashMap<>();

        // Initialize last 6 months
        LocalDate now = LocalDate.now();
        for (int i = 5; i >= 0; i--) {
            LocalDate date = now.minusMonths(i);
            String monthName = date.getMonth().getDisplayName(TextStyle.SHORT, new Locale("es", "ES"));
            salesByMonth.put(monthName, BigDecimal.ZERO);
        }

        // Aggregate data
        for (Order order : allOrders) {
            String month = order.getOrderDate().getMonth().getDisplayName(TextStyle.SHORT, new Locale("es", "ES"));
            if (salesByMonth.containsKey(month)) {
                salesByMonth.put(month, salesByMonth.get(month).add(order.getTotal()));
            }
        }

        List<String> labels = new ArrayList<>(salesByMonth.keySet());
        List<BigDecimal> values = new ArrayList<>(salesByMonth.values());

        return ResponseEntity.ok(new AnalyticsDTO(labels, values, "Ingresos Mensuales"));
    }
}

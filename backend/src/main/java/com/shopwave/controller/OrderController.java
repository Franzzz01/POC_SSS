package com.shopwave.controller;

import com.shopwave.model.Order;
import com.shopwave.service.JwtService;
import com.shopwave.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final JwtService jwtService;

    private String extractUsername(String authHeader) {
        return jwtService.extractUsername(authHeader.replace("Bearer ", ""));
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestHeader("Authorization") String auth) {
        try {
            Order order = orderService.placeOrder(extractUsername(auth));
            return ResponseEntity.ok(order);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public List<Order> getOrders(@RequestHeader("Authorization") String auth) {
        return orderService.getUserOrders(extractUsername(auth));
    }
}

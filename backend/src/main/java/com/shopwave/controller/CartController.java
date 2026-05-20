package com.shopwave.controller;

import com.shopwave.model.Cart;
import com.shopwave.service.CartService;
import com.shopwave.service.JwtService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final JwtService jwtService;

    private String extractUsername(String authHeader) {
        return jwtService.extractUsername(authHeader.replace("Bearer ", ""));
    }

    @GetMapping
    public Cart getCart(@RequestHeader("Authorization") String auth) {
        return cartService.getCart(extractUsername(auth));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addItem(@RequestHeader("Authorization") String auth,
                                     @RequestBody AddItemRequest req) {
        try {
            Cart cart = cartService.addItem(extractUsername(auth), req.getProductId(), req.getQty());
            return ResponseEntity.ok(cart);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/item/{productId}")
    public Cart updateItem(@RequestHeader("Authorization") String auth,
                           @PathVariable int productId,
                           @RequestBody Map<String, Integer> body) {
        return cartService.updateItem(extractUsername(auth), productId, body.get("qty"));
    }

    @DeleteMapping("/item/{productId}")
    public Cart removeItem(@RequestHeader("Authorization") String auth,
                           @PathVariable int productId) {
        return cartService.removeItem(extractUsername(auth), productId);
    }

    @DeleteMapping("/clear")
    public Cart clearCart(@RequestHeader("Authorization") String auth) {
        return cartService.clearCart(extractUsername(auth));
    }

    @Data
    static class AddItemRequest {
        private int productId;
        private int qty = 1;
    }
}

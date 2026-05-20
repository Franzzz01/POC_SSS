package com.shopwave.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private String id;
    private String username;
    private List<Cart.CartItem> items;
    private double total;
    private String status;
    private LocalDateTime createdAt;

    public static Order from(Cart cart) {
        Order o = new Order();
        o.id = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        o.username = cart.getUsername();
        o.items = cart.getItems();
        o.total = cart.getTotal();
        o.status = "CONFIRMED";
        o.createdAt = LocalDateTime.now();
        return o;
    }
}

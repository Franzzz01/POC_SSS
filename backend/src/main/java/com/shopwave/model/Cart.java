package com.shopwave.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    private String username;
    private List<CartItem> items = new ArrayList<>();

    public double getTotal() {
        return items.stream().mapToDouble(i -> i.getPrice() * i.getQty()).sum();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CartItem {
        private int id;
        private String name;
        private double price;
        private String image;
        private int qty;
    }
}

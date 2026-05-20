package com.shopwave.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.shopwave.model.Cart;
import com.shopwave.model.Order;
import com.shopwave.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private static final String ORDERS_FILE = "orders.json";
    private static final TypeReference<List<Order>> ORDER_LIST_TYPE = new TypeReference<>() {};

    private final FileRepository fileRepository;
    private final CartService cartService;

    public Order placeOrder(String username) {
        Cart cart = cartService.getCart(username);
        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cart is empty");
        }

        Order order = Order.from(cart);
        fileRepository.save(ORDERS_FILE, ORDER_LIST_TYPE, order,
                o -> o.getId().equals(order.getId()), false);

        // Clear the cart after order
        cartService.clearCart(username);

        return order;
    }

    public List<Order> getUserOrders(String username) {
        return fileRepository.readAll(ORDERS_FILE, ORDER_LIST_TYPE).stream()
                .filter(o -> o.getUsername().equals(username))
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .toList();
    }
}

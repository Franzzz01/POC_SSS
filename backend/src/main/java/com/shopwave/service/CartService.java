package com.shopwave.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.shopwave.model.Cart;
import com.shopwave.model.Product;
import com.shopwave.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private static final String CARTS_FILE = "carts.json";
    private static final TypeReference<List<Cart>> CART_LIST_TYPE = new TypeReference<>() {};

    private final FileRepository fileRepository;
    private final ProductService productService;

    public Cart getCart(String username) {
        return fileRepository.findOne(CARTS_FILE, CART_LIST_TYPE,
                c -> c.getUsername().equals(username))
                .orElse(new Cart(username, new ArrayList<>()));
    }

    public Cart addItem(String username, int productId, int qty) {
        Product product = productService.getById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));

        Cart cart = getCart(username);
        List<Cart.CartItem> items = new ArrayList<>(cart.getItems());

        Optional<Cart.CartItem> existing = items.stream()
                .filter(i -> i.getId() == productId).findFirst();

        if (existing.isPresent()) {
            items = items.stream().map(i -> {
                if (i.getId() == productId) {
                    return new Cart.CartItem(i.getId(), i.getName(), i.getPrice(), i.getImage(), i.getQty() + qty);
                }
                return i;
            }).toList();
        } else {
            items.add(new Cart.CartItem(product.getId(), product.getName(),
                    product.getPrice(), product.getImage(), qty));
        }

        Cart updated = new Cart(username, new ArrayList<>(items));
        saveCart(updated);
        return updated;
    }

    public Cart updateItem(String username, int productId, int qty) {
        Cart cart = getCart(username);
        List<Cart.CartItem> items = new ArrayList<>(cart.getItems());

        if (qty <= 0) {
            items.removeIf(i -> i.getId() == productId);
        } else {
            items = items.stream().map(i -> {
                if (i.getId() == productId) {
                    return new Cart.CartItem(i.getId(), i.getName(), i.getPrice(), i.getImage(), qty);
                }
                return i;
            }).toList();
        }

        Cart updated = new Cart(username, new ArrayList<>(items));
        saveCart(updated);
        return updated;
    }

    public Cart removeItem(String username, int productId) {
        return updateItem(username, productId, 0);
    }

    public Cart clearCart(String username) {
        Cart empty = new Cart(username, new ArrayList<>());
        saveCart(empty);
        return empty;
    }

    private void saveCart(Cart cart) {
        fileRepository.save(CARTS_FILE, CART_LIST_TYPE, cart,
                c -> c.getUsername().equals(cart.getUsername()), true);
    }
}

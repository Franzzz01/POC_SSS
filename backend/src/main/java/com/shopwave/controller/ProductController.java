package com.shopwave.controller;

import com.shopwave.model.Product;
import com.shopwave.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<Product> getAll(@RequestParam(required = false) String category,
                                @RequestParam(required = false) String search) {
        if (search != null && !search.isBlank()) return productService.search(search);
        if (category != null && !category.isBlank()) return productService.getByCategory(category);
        return productService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable int id) {
        return productService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

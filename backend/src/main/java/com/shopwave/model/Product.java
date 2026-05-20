package com.shopwave.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private int id;
    private String name;
    private String category;
    private double price;
    private double rating;
    private int reviews;
    private String description;
    private String image;
    private String badge;
}

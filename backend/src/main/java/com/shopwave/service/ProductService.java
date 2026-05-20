package com.shopwave.service;

import com.shopwave.model.Product;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    // All products stored in-memory — no database needed
    private final List<Product> catalog = Arrays.asList(
        // Phones
        new Product(1, "Samsung Galaxy S25 Ultra", "phones", 1199, 4.8, 2341,
            "The pinnacle of Android excellence. A titanium frame meets a 200MP camera system that captures the world in extraordinary detail.",
            "https://images.unsplash.com/photo-1610945415295-d9bbf067e59c?w=400&q=80", "Bestseller"),
        new Product(2, "iPhone 16 Pro Max", "phones", 1299, 4.9, 4120,
            "Apple Intelligence meets titanium design. The A18 Pro chip delivers desktop-class performance in your pocket.",
            "https://images.unsplash.com/photo-1592750475338-74b7b21085ab?w=400&q=80", "New"),
        new Product(3, "Google Pixel 9 Pro", "phones", 999, 4.7, 1876,
            "The smartest camera phone ever made. Google's AI photography engine turns every moment into a masterpiece.",
            "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=400&q=80", null),
        new Product(4, "OnePlus 13", "phones", 799, 4.6, 987,
            "Blazing fast charging meets flagship performance. 100W SuperVOOC fills your battery in 25 minutes.",
            "https://images.unsplash.com/photo-1526045612212-70caf35c14df?w=400&q=80", "Value Pick"),
        new Product(5, "Sony Xperia 1 VI", "phones", 1099, 4.5, 654,
            "Cinema-grade color science in a phone. Perfect for creators who demand pro-level video.",
            "https://images.unsplash.com/photo-1570891836654-d4909d51b7a9?w=400&q=80", null),
        // Laptops
        new Product(6, "MacBook Pro 16\" M4", "laptops", 2499, 4.9, 3201,
            "The M4 Pro chip. Up to 24 CPU cores. 48GB unified memory. An absolute powerhouse wrapped in precision aluminum.",
            "https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=400&q=80", "Editor's Choice"),
        new Product(7, "Dell XPS 15", "laptops", 1899, 4.7, 1654,
            "An OLED display so vivid you'll question reality. Thunderbolt 5 connectivity meets Intel i9.",
            "https://images.unsplash.com/photo-1593642632559-0c6d3fc62b89?w=400&q=80", "New"),
        new Product(8, "ASUS ROG Zephyrus G16", "laptops", 1699, 4.8, 2109,
            "RTX 4080 in a 1.85kg frame. The thinnest gaming powerhouse ever engineered.",
            "https://images.unsplash.com/photo-1603302576837-37561b2e2302?w=400&q=80", "Gaming"),
        new Product(9, "Lenovo ThinkPad X1 Carbon", "laptops", 1549, 4.6, 891,
            "Business-class durability meets ultrabook elegance. MIL-STD certified, under 1.12kg.",
            "https://images.unsplash.com/photo-1496181133206-80ce9b88a853?w=400&q=80", null),
        new Product(10, "Microsoft Surface Laptop 6", "laptops", 1299, 4.5, 743,
            "The perfect Windows experience. PixelSense touchscreen, Snapdragon X Elite, all-day battery.",
            "https://images.unsplash.com/photo-1541807084-5c52b6b3adef?w=400&q=80", null),
        // Monitors
        new Product(11, "LG UltraGear 27\" 4K OLED", "monitors", 799, 4.9, 1876,
            "0.03ms response time. 240Hz. OLED blacks so deep they're invisible. Gaming transcended.",
            "https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?w=400&q=80", "Top Rated"),
        new Product(12, "Samsung Odyssey G9 49\"", "monitors", 1199, 4.7, 1203,
            "A 1000R curved ultra-wide that wraps around your vision. Dual QHD, 240Hz, Neo QLED.",
            "https://images.unsplash.com/photo-1593642632559-0c6d3fc62b89?w=400&q=80", "Ultrawide"),
        new Product(13, "Apple Studio Display", "monitors", 1599, 4.8, 2341,
            "5K Retina. 218 pixels per inch. True Tone. 600 nits. Designed for creators who demand perfection.",
            "https://images.unsplash.com/photo-1498050108023-c5249f4df085?w=400&q=80", null),
        new Product(14, "Dell U2723QX 27\" 4K", "monitors", 649, 4.6, 987,
            "USB-C hub, IPS Black panel, 98% DCI-P3. The ultimate productivity display.",
            "https://images.unsplash.com/photo-1555421689-491a97ff2040?w=400&q=80", null),
        new Product(15, "ASUS ProArt 32\" 4K", "monitors", 899, 4.7, 654,
            "Delta E < 2 factory calibration. Hardware calibration support. A creator's true companion.",
            "https://images.unsplash.com/photo-1587831990711-23ca6441447b?w=400&q=80", "Creator Pick")
    );

    public List<Product> getAll() { return catalog; }

    public List<Product> getByCategory(String category) {
        return catalog.stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .toList();
    }

    public Optional<Product> getById(int id) {
        return catalog.stream().filter(p -> p.getId() == id).findFirst();
    }

    public List<Product> search(String query) {
        String q = query.toLowerCase();
        return catalog.stream()
                .filter(p -> p.getName().toLowerCase().contains(q) ||
                             p.getDescription().toLowerCase().contains(q) ||
                             p.getCategory().toLowerCase().contains(q))
                .toList();
    }
}

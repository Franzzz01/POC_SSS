package com.shopwave;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ShopWaveApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShopWaveApplication.class, args);
        System.out.println("\n✅ ShopWave backend running at http://localhost:8080");
        System.out.println("📁 Data stored in ./data directory (no database needed)\n");
    }
}

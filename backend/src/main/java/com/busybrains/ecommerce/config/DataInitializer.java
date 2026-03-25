package com.busybrains.ecommerce.config;

import com.busybrains.ecommerce.model.Product;
import com.busybrains.ecommerce.model.Role;
import com.busybrains.ecommerce.model.User;
import com.busybrains.ecommerce.repository.ProductRepository;
import com.busybrains.ecommerce.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner seedData(UserRepository userRepository, ProductRepository productRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (!userRepository.existsByUsername("admin")) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setEmail("admin@example.com");
                admin.setPassword(passwordEncoder.encode("password"));
                admin.setRole(Role.ROLE_ADMIN);
                userRepository.save(admin);
            }
            if (!userRepository.existsByUsername("user")) {
                User user = new User();
                user.setUsername("user");
                user.setEmail("user@example.com");
                user.setPassword(passwordEncoder.encode("password"));
                user.setRole(Role.ROLE_USER);
                userRepository.save(user);
            }
            if (productRepository.count() == 0) {
                productRepository.saveAll(List.of(
                        product("Noise Cancelling Headphones", "Wireless headset with adaptive noise cancellation.", "199.99"),
                        product("Mechanical Keyboard", "Compact RGB keyboard with tactile switches.", "89.00"),
                        product("4K Monitor", "27-inch display for design and development workflows.", "299.50")
                ));
            }
        };
    }

    private Product product(String name, String description, String price) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(new BigDecimal(price));
        return product;
    }
}

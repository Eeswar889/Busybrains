package com.busybrains.ecommerce.service;

import com.busybrains.ecommerce.dto.ProductRequest;
import com.busybrains.ecommerce.model.Product;
import com.busybrains.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product create(ProductRequest request) {
        Product product = new Product();
        apply(product, request);
        return productRepository.save(product);
    }

    public Product update(Long id, ProductRequest request) {
        Product product = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Product not found"));
        apply(product, request);
        return productRepository.save(product);
    }

    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new IllegalArgumentException("Product not found");
        }
        productRepository.deleteById(id);
    }

    private void apply(Product product, ProductRequest request) {
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
    }
}

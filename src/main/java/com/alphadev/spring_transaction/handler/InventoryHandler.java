package com.alphadev.spring_transaction.handler;

import com.alphadev.spring_transaction.entity.Product;
import com.alphadev.spring_transaction.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class InventoryHandler {

    private final ProductRepository productRepository;

    public InventoryHandler(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product updateProductDetails(Product product) {
        //throe an exception to simulate transaction use case
        if(product.getPrice() > 5000) {
            throw new RuntimeException("DB Crashed ......");
        }
        return productRepository.save(product);
    }

    public Product getProduct(int id) {
        return productRepository.findById(id)
                .orElseThrow(
                        () -> new RuntimeException("Product not available with id : "+ id)
                );
    }
}

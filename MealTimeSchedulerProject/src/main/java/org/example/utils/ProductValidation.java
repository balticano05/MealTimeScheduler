package org.example.utils;

import org.example.entity.Product;

public class ProductValidation {

    public static void validateProduct(Product product) {
        if (product.getName() == null || product.getName().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        if (product.getCalories() < 0) {
            throw new IllegalArgumentException("Calories cannot be negative");
        }
    }
}

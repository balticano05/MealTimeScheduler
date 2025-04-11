package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.entity.Product;
import org.example.repository.DbRepository;

import java.util.List;

@RequiredArgsConstructor
public class ProductService {

    private final DbRepository dbRepository;

    public List<Product> findProductsByCategory(String category) {
        return dbRepository.findProductsByCategory(category);
    }

    public void addProduct(Product product, String category) {
        dbRepository.addProduct(product, category);
        dbRepository.save();
    }

    public void updateProduct(Product product) {
        dbRepository.update(product);
        dbRepository.save();
    }

    public void deleteProductByName(String productName) {
        dbRepository.delete(productName);
        dbRepository.save();
    }

    public List<String> findAllCategoryNames() {
        return dbRepository.findAllCategoryNames();
    }

    public DbRepository getRepository() {
        return dbRepository;
    }

    public List<Product> findAllProducts() {
        return dbRepository.findALlProducts();
    }

}

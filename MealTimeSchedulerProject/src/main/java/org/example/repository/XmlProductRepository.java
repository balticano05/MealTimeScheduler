package org.example.repository;

import org.example.entity.Category;
import org.example.entity.Db;
import org.example.entity.Product;
import org.example.utils.XmlUtils;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class XmlProductRepository {

    private final String xmlFilePath;
    private Db db;

    public XmlProductRepository(String xmlFilePath) {
        this.xmlFilePath = xmlFilePath;
        loadData();
    }

    private void loadData() {
        try {
            db = XmlUtils.deserializeFromXml(xmlFilePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load XML data", e);
        }
    }

    public List<Product> findAll() {
        return db.getCategories().stream()
                .flatMap(c -> c.getProducts().stream())
                .collect(Collectors.toList());
    }

    public List<Product> findByCategory(String category) {
        return db.getCategories().stream()
                .filter(c -> c.getName().equalsIgnoreCase(category))
                .flatMap(c -> c.getProducts().stream())
                .collect(Collectors.toList());
    }

    private void saveData() {
        try {
            XmlUtils.serializeToXml(db, xmlFilePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save XML data", e);
        }
    }

    public void save(Product product) {
        Category category = db.getCategories().stream()
                .filter(c -> c.getProducts().contains(product))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        category.getProducts().add(product);
        saveData();
    }

    public void update(Product product) {
        db.getCategories().forEach(category ->
                category.getProducts().replaceAll(p ->
                        p.getName().equals(product.getName()) ? product : p
                )
        );
        saveData();
    }

    public void delete(String productName) {
        db.getCategories().forEach(category ->
                category.getProducts().removeIf(p -> p.getName().equals(productName))
        );
        saveData();
    }
}
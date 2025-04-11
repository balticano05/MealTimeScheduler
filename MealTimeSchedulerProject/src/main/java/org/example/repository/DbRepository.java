package org.example.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.entity.Category;
import org.example.entity.Db;
import org.example.entity.Product;
import org.example.utils.XmlUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Setter
@Getter
@AllArgsConstructor
public class DbRepository {

    private Db db;
    private final String dataPath;

    public DbRepository() {
        this("src/main/resources/food-data.xml");
    }

    public DbRepository(String dataPath) {
        this.dataPath = dataPath;
        try {
            this.db = XmlUtils.deserializeFromXml(dataPath);
        } catch (IOException e) {
            this.db = new Db();
            this.db.setCategories(new ArrayList<>());
        }
    }

    public void save() {
        try {
            XmlUtils.serializeToXml(db, dataPath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save data", e);
        }
    }

    public List<Category> findCategories() {
        return db.getCategories();
    }

    public List<Category> findCategories(String categoryName) {
        return db.getCategories().stream()
                .filter(e -> e.getName().equals(categoryName))
                .toList();
    }

    public List<Product> findProductsByCategory(String categoryName) {
        return db.getCategories().stream()
                .filter(c -> c.getName().equals(categoryName))
                .findFirst()
                .map(Category::getProducts)
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + categoryName));
    }

    public void addProduct(Product product, String categoryName) {
        Category category = db.getCategories().stream()
                .filter(c -> c.getName().equals(categoryName))
                .findFirst()
                .orElseGet(() -> {
                    Category newCategory = new Category(categoryName, "", new ArrayList<>());
                    db.getCategories().add(newCategory);
                    return newCategory;
                });

        category.getProducts().add(product);
    }

    public void update(Product product) {
        boolean updated = db.getCategories().stream()
                .anyMatch(category -> {
                    Optional<Product> toUpdate = category.getProducts().stream()
                            .filter(p -> p.getName().equals(product.getName()))
                            .findFirst();

                    if (toUpdate.isPresent()) {
                        int index = category.getProducts().indexOf(toUpdate.get());
                        category.getProducts().set(index, product);
                        return true;
                    }
                    return false;
                });

        if (!updated) {
            throw new IllegalArgumentException("Product not found: " + product.getName());
        }
    }

    public void delete(String productName) {
        boolean removed = db.getCategories().stream()
                .anyMatch(category -> category.getProducts().removeIf(p -> p.getName().equals(productName)));

        if (!removed) {
            throw new IllegalArgumentException("Product not found: " + productName);
        }
    }

    public List<String> findAllCategoryNames() {
        return db.getCategories().stream()
                .map(Category::getName)
                .toList();
    }

    public void addCategory(Category category) {
        if(db.getCategories().stream().anyMatch(c -> c.getName().equalsIgnoreCase(category.getName()))) {
            throw new IllegalArgumentException("Category already exists: " + category.getName());
        }
        db.getCategories().add(category);
        save();
    }

    public void updateCategory(String oldName, Category updatedCategory) {
        Category existing = db.getCategories().stream()
                .filter(c -> c.getName().equals(oldName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + oldName));

        if(!oldName.equals(updatedCategory.getName()) &&
                db.getCategories().stream().anyMatch(c -> c.getName().equals(updatedCategory.getName()))) {
            throw new IllegalArgumentException("Category name already exists: " + updatedCategory.getName());
        }

        existing.setName(updatedCategory.getName());
        existing.setDescription(updatedCategory.getDescription());
        save();
    }

    public void deleteCategory(String categoryName) {
        Category category = db.getCategories().stream()
                .filter(c -> c.getName().equals(categoryName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + categoryName));

        if (!category.getProducts().isEmpty()) {
            throw new IllegalStateException("Cannot delete non-empty category");
        }

        db.getCategories().remove(category);
        save();
    }

}
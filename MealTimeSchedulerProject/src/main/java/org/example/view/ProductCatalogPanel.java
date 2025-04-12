package org.example.view;

import lombok.AllArgsConstructor;
import org.example.entity.Product;
import org.example.service.PlanService;
import org.example.service.ProductService;
import org.example.utils.Parser;

import javax.swing.*;
import java.awt.*;
import java.util.List;

@AllArgsConstructor
public class ProductCatalogPanel extends JPanel {

    private ProductService productService;
    private JComboBox<String> categoryComboBox;
    private PlanService planService;
    private JTable productTable;

    public ProductCatalogPanel(ProductService productService, PlanService planService) {
        this.productService = productService;
        this.planService = planService;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel filterPanel = new JPanel();
        categoryComboBox = new JComboBox<>();
        categoryComboBox.addActionListener(e -> filterProducts());
        filterPanel.add(new JLabel("Категория:"));
        filterPanel.add(categoryComboBox);

        JButton addButton = new JButton("Добавить");
        addButton.addActionListener(e -> showAddProductDialog());

        productTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(productTable);

        JButton deleteButton = new JButton("Удалить");
        deleteButton.addActionListener(e -> deleteSelectedProduct());
        filterPanel.add(deleteButton);

        JButton addToPlanButton = new JButton("Добавить в план");
        addToPlanButton.addActionListener(e -> addToMeal());
        filterPanel.add(addToPlanButton);

        JButton manageCategoriesButton = new JButton("Управление категориями");
        manageCategoriesButton.addActionListener(e -> showCategoryManagementDialog());
        filterPanel.add(manageCategoriesButton);

        add(filterPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(addButton, BorderLayout.SOUTH);

        loadCategories();
    }

    private void loadCategories() {
        List<String> categories = productService.findAllCategoryNames();
        categories.forEach(categoryComboBox::addItem);
    }

    private void filterProducts() {
        String category = (String) categoryComboBox.getSelectedItem();
        List<Product> products = productService.findProductsByCategory(category);
        updateProductTable(products);
    }

    private void updateProductTable(List<Product> products) {
        ProductTableModel model = new ProductTableModel(products);
        productTable.setModel(model);
    }
    private void showAddProductDialog() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Добавить новый продукт");
        dialog.setLayout(new GridLayout(0, 2, 5, 5));
        dialog.setSize(400, 300);

        JTextField nameField = new JTextField();
        JComboBox<String> categoryCombo = new JComboBox<>();
        JSpinner caloriesSpinner = createDecimalSpinner(0.0, 0.0, 1000.0, 1.0, "#0");
        JSpinner proteinSpinner = createDecimalSpinner(0.0, 0.0, 100.0, 0.1, "#0.00");
        JSpinner fatsSpinner = createDecimalSpinner(0.0, 0.0, 100.0, 0.1, "#0.00");
        JSpinner carbsSpinner = createDecimalSpinner(0.0, 0.0, 100.0, 0.1, "#0.00");

        List<String> categories = productService.findAllCategoryNames();
        categories.forEach(categoryCombo::addItem);
        categoryCombo.setEditable(true);

        dialog.add(new JLabel("Название:"));
        dialog.add(nameField);
        dialog.add(new JLabel("Категория:"));
        dialog.add(categoryCombo);
        dialog.add(new JLabel("Калории:"));
        dialog.add(caloriesSpinner);
        dialog.add(new JLabel("Белки:"));
        dialog.add(proteinSpinner);
        dialog.add(new JLabel("Жиры:"));
        dialog.add(fatsSpinner);
        dialog.add(new JLabel("Углеводы:"));
        dialog.add(carbsSpinner);

        JButton saveButton = new JButton("Сохранить");
        JButton cancelButton = new JButton("Отмена");

        saveButton.addActionListener(e -> {
            try {
                Product product = new Product();
                product.setName(nameField.getText());
                product.setCalories((Double) caloriesSpinner.getValue());
                product.setProtein(Parser.formatDoubleValue(proteinSpinner.getValue()));
                product.setFats(Parser.formatDoubleValue(fatsSpinner.getValue()));
                product.setCarbs(Parser.formatDoubleValue(carbsSpinner.getValue()));

                String category = (String) categoryCombo.getSelectedItem();

                productService.addProduct(product, category);
                filterProducts();
                loadCategories();
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        dialog,
                        "Ошибка при сохранении: " + ex.getMessage(),
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.add(saveButton);
        dialog.add(cancelButton);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private JSpinner createDecimalSpinner(double initialValue,
                                          double min,
                                          double max,
                                          double step,
                                          String pattern) {
        SpinnerNumberModel model = new SpinnerNumberModel(
                initialValue,
                min,
                max,
                step
        );

        JSpinner spinner = new JSpinner(model);

        JSpinner.NumberEditor editor = new JSpinner.NumberEditor(
                spinner,
                pattern
        );

        spinner.setEditor(editor);

        return spinner;
    }

    private void deleteSelectedProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow >= 0) {
            Product product = ((ProductTableModel) productTable.getModel())
                    .getProductAt(selectedRow);
            productService.deleteProductByName(product.getName());
            filterProducts();
            loadCategories();
        }
    }

    private void showCategoryManagementDialog() {
        CategoryManagementDialog dialog = new CategoryManagementDialog(
                (Frame) SwingUtilities.getWindowAncestor(this),
                productService.getRepository()
        );
        dialog.setVisible(true);
        loadCategories();
        filterProducts();
    }

    private void addToMeal() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow >= 0) {
            Product product = ((ProductTableModel) productTable.getModel()).getProductAt(selectedRow);
            List<String> mealNames = planService.getMealNames(); // Новый метод в PlanService
            AddToMealDialog dialog = new AddToMealDialog(
                    (Frame) SwingUtilities.getWindowAncestor(this),
                    mealNames
            );
            if (dialog.showDialog()) {
                planService.addProductToMeal(
                        dialog.getSelectedMeal(),
                        product,
                        dialog.getWeight()
                );
            }
        }
    }

}

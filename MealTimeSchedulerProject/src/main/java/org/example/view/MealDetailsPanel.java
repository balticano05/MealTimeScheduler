package org.example.view;

import org.example.entity.Meal;
import org.example.entity.Product;
import org.example.service.PlanService;
import org.example.service.ProductService;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.Vector;

public class MealDetailsPanel extends JPanel {

    private final Meal meal;
    private final ProductService productService;
    private final PlanService planService;
    private MealProductsTableModel tableModel;
    private JTable productsTable;

    public MealDetailsPanel(Meal meal, ProductService productService, PlanService planService) {
        this.meal = meal;
        this.productService = productService;
        this.planService = planService;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        tableModel = new MealProductsTableModel(meal.getItems());
        productsTable = new JTable(tableModel);

        // Таблица продуктов в приеме пищи
        productsTable = new JTable(new MealProductsTableModel(meal.getItems()));
        productsTable.setRowHeight(25);

        // Настройка колонки с весом
        JSpinner weightSpinner = new JSpinner(new SpinnerNumberModel(100.0, 1.0, 1000.0, 1.0));
        productsTable.getColumnModel().getColumn(1).setCellEditor(new SpinnerEditor());

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Добавить продукт");
        JButton removeButton = new JButton("Удалить");
        JButton saveButton = new JButton("Сохранить изменения");

        addButton.addActionListener(e -> showAddProductDialog());
        removeButton.addActionListener(e -> removeSelectedProduct());
        saveButton.addActionListener(e -> saveChanges());

        controlPanel.add(addButton);
        controlPanel.add(removeButton);
        controlPanel.add(saveButton);

        add(new JScrollPane(productsTable), BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }

    private void showAddProductDialog() {

        JDialog dialog = new JDialog();
        dialog.setLayout(new BorderLayout());

        JList<Product> productList = new JList<>(new Vector<>(productService.findAllProducts()));
        productList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton selectButton = new JButton("Выбрать");

        selectButton.addActionListener(e -> {
            Product selectedProduct = productList.getSelectedValue();
            if (selectedProduct != null) {
                planService.addProductToMeal(
                        meal.getName(), // Используем имя приема пищи
                        selectedProduct,
                        100.0
                );
            }
        });

        dialog.add(new JScrollPane(productList), BorderLayout.CENTER);
        dialog.add(selectButton, BorderLayout.SOUTH);
        dialog.setSize(300, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void removeSelectedProduct() {
        int selectedRow = productsTable.getSelectedRow();
        if (selectedRow >= 0) {
            meal.getItems().remove(selectedRow);
            ((AbstractTableModel)productsTable.getModel()).fireTableDataChanged();
        }
    }

    private void saveChanges() {
        // Логика сохранения изменений, если требуется
        JOptionPane.showMessageDialog(this, "Изменения сохранены!");
    }



}

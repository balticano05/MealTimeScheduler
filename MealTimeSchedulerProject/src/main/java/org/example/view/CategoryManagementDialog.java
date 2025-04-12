package org.example.view;

import org.example.entity.Category;
import org.example.repository.DbRepository;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.List;

public class CategoryManagementDialog extends JDialog {

    private JTable table;
    private final DbRepository repository;
    private List<Category> categories;

    public CategoryManagementDialog(Frame parent, DbRepository repository) {
        super(parent, "Управление категориями", true);
        this.repository = repository;
        initComponents();
        loadCategories();
    }

    private void initComponents() {

        setLayout(new BorderLayout());
        setSize(500, 400);

        table = new JTable(new AbstractTableModel() {
            String[] columns = {"Название", "Описание", "Продуктов"};

            @Override
            public int getRowCount() {
                return categories.size();
            }

            @Override
            public int getColumnCount() {
                return columns.length;
            }

            @Override
            public Object getValueAt(int row, int column) {
                Category c = categories.get(row);
                return switch (column) {
                    case 0 -> c.getName();
                    case 1 -> c.getDescription();
                    case 2 -> c.getProducts().size();
                    default -> null;
                };
            }

            @Override
            public String getColumnName(int column) {
                return columns[column];
            }
        });

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Добавить");
        JButton editButton = new JButton("Изменить");
        JButton deleteButton = new JButton("Удалить");

        addButton.addActionListener(e -> addCategory());
        editButton.addActionListener(e -> editCategory());
        deleteButton.addActionListener(e -> deleteCategory());

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadCategories() {
        categories = repository.findCategories();
        ((AbstractTableModel) table.getModel()).fireTableDataChanged();
    }

    private void addCategory() {
        CategoryEditorDialog dialog = new CategoryEditorDialog((Frame) getParent(), null);
        if(dialog.showDialog()) {
            repository.addCategory(dialog.getCategory());
            loadCategories();
        }
    }

    private void editCategory() {
        int row = table.getSelectedRow();
        if(row >= 0) {
            Category selected = categories.get(row);
            CategoryEditorDialog dialog = new CategoryEditorDialog((Frame) getParent(), selected);
            if(dialog.showDialog()) {
                repository.updateCategory(selected.getName(), dialog.getCategory());
                loadCategories();
            }
        }
    }

    private void deleteCategory() {
        int row = table.getSelectedRow();
        if(row >= 0) {
            Category category = categories.get(row);
            try {
                repository.deleteCategory(category.getName());
                loadCategories();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}

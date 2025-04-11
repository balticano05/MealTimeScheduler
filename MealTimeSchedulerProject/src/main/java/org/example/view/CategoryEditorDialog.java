package org.example.view;

import org.example.entity.Category;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class CategoryEditorDialog extends JDialog {

    private boolean saved = false;
    private Category category;

    private JTextField nameField;
    private JTextArea descriptionArea;

    public CategoryEditorDialog(Frame parent, Category category) {
        super(parent, "Редактирование категории", true);
        this.category = category != null ? category : new Category("", "", new ArrayList<>());
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(5, 5));
        setSize(400, 300);

        // Поля ввода
        JPanel inputPanel = new JPanel(new GridLayout(2, 1, 5, 5));

        JPanel namePanel = new JPanel(new BorderLayout());
        namePanel.add(new JLabel("Название:"), BorderLayout.NORTH);
        nameField = new JTextField(category.getName());
        namePanel.add(nameField, BorderLayout.CENTER);

        JPanel descPanel = new JPanel(new BorderLayout());
        descPanel.add(new JLabel("Описание:"), BorderLayout.NORTH);
        descriptionArea = new JTextArea(category.getDescription());
        descPanel.add(new JScrollPane(descriptionArea), BorderLayout.CENTER);

        inputPanel.add(namePanel);
        inputPanel.add(descPanel);

        // Кнопки
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Сохранить");
        JButton cancelButton = new JButton("Отмена");

        saveButton.addActionListener(e -> {
            if(validateInput()) {
                category.setName(nameField.getText().trim());
                category.setDescription(descriptionArea.getText().trim());
                saved = true;
                dispose();
            }
        });

        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private boolean validateInput() {
        if(nameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Название категории не может быть пустым!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public boolean showDialog() {
        setVisible(true);
        return saved;
    }

    public Category getCategory() {
        return category;
    }

}

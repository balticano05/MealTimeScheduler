package org.example.view;

import lombok.Getter;
import org.example.entity.Category;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

@Getter
public class CategoryEditorDialog extends JDialog {

    private boolean saved = false;
    private Category category;
    private JTextField nameField;
    private JTextArea descriptionArea;

    public CategoryEditorDialog(Frame parent, Category category) {
        super(parent, "Редактирование категории", true);
        initializeCategory(category);
        initializeDialog();
    }

    private void initializeCategory(Category category) {
        this.category = category != null ? category : new Category("", "", new ArrayList<>());
    }

    private void initializeDialog() {
        setLayout(new BorderLayout(10, 10));
        setupMainComponents();
        setupDialogProperties();
    }

    private void setupMainComponents() {
        add(createInputPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new GridLayout(2, 1, 5, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputPanel.add(createNamePanel());
        inputPanel.add(createDescriptionPanel());
        return inputPanel;
    }

    private JPanel createNamePanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.add(createLabel("Название:"), BorderLayout.NORTH);
        panel.add(createNameField(), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createDescriptionPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.add(createLabel("Описание:"), BorderLayout.NORTH);
        panel.add(createDescriptionScrollPane(), BorderLayout.CENTER);
        return panel;
    }

    private JLabel createLabel(String text) {
        return new JLabel(text);
    }

    private JTextField createNameField() {
        nameField = new JTextField(category.getName());
        nameField.setPreferredSize(new Dimension(300, 25));
        return nameField;
    }

    private JScrollPane createDescriptionScrollPane() {
        descriptionArea = new JTextArea(category.getDescription(), 5, 20);
        descriptionArea.setLineWrap(true);
        return new JScrollPane(descriptionArea);
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 10));

        buttonPanel.add(createSaveButton());
        buttonPanel.add(createCancelButton());
        return buttonPanel;
    }

    private JButton createSaveButton() {
        JButton button = new JButton("Сохранить");
        button.addActionListener(e -> handleSaveAction());
        return button;
    }

    private JButton createCancelButton() {
        JButton button = new JButton("Отмена");
        button.addActionListener(e -> dispose());
        return button;
    }

    private void handleSaveAction() {
        if(validateInput()) {
            updateCategoryData();
            saved = true;
            dispose();
        }
    }

    private void updateCategoryData() {
        category.setName(nameField.getText().trim());
        category.setDescription(descriptionArea.getText().trim());
    }

    private void setupDialogProperties() {
        setSize(400, 300);
        setLocationRelativeTo(getParent());
        setResizable(false);
    }

    private boolean validateInput() {
        if(nameField.getText().trim().isEmpty()) {
            showValidationError("Название категории не может быть пустым!");
            return false;
        }
        return true;
    }

    private void showValidationError(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Ошибка",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public boolean showDialog() {
        setVisible(true);
        return saved;
    }

}
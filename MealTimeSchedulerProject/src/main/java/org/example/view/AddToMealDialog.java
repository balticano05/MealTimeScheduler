package org.example.view;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AddToMealDialog extends JDialog {

    private JComboBox<String> mealCombo;
    private JSpinner weightSpinner;
    private boolean confirmed = false;

    public AddToMealDialog(Frame parent, List<String> mealNames) {
        super(parent, "Добавить в прием пищи", true);
        initializeDialog(mealNames);
    }

    private void initializeDialog(List<String> mealNames) {
        setupMainComponents(mealNames);
        setupDialogProperties();
    }

    private void setupMainComponents(List<String> mealNames) {
        setLayout(new BorderLayout(10, 10));
        mealCombo = createMealComboBox(mealNames);
        weightSpinner = createWeightSpinner();
        add(createFormPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JComboBox<String> createMealComboBox(List<String> mealNames) {
        return new JComboBox<>(mealNames.toArray(new String[0]));
    }

    private JSpinner createWeightSpinner() {
        return new JSpinner(new SpinnerNumberModel(100.0, 1.0, 1000.0, 1.0));
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        formPanel.add(createLabel("Прием пищи:"));
        formPanel.add(mealCombo);
        formPanel.add(createLabel("Вес (г):"));
        formPanel.add(weightSpinner);
        return formPanel;
    }

    private JLabel createLabel(String text) {
        return new JLabel(text);
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        buttonPanel.add(createOkButton());
        buttonPanel.add(createCancelButton());
        return buttonPanel;
    }

    private JButton createOkButton() {
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> confirmAndClose());
        return okButton;
    }

    private JButton createCancelButton() {
        JButton cancelButton = new JButton("Отмена");
        cancelButton.addActionListener(e -> dispose());
        return cancelButton;
    }

    private void setupDialogProperties() {
        pack();
        setLocationRelativeTo(getParent());
        setResizable(false);
    }

    private void confirmAndClose() {
        confirmed = true;
        dispose();
    }

    public boolean showDialog() {
        setVisible(true);
        return confirmed;
    }

    public String getSelectedMeal() {
        return (String) mealCombo.getSelectedItem();
    }

    public double getWeight() {
        return (Double) weightSpinner.getValue();
    }

}
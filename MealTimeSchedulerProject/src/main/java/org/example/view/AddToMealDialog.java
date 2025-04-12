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
        initComponents(mealNames);
    }

    private void initComponents(List<String> mealNames) {
        setLayout(new GridLayout(3, 2, 5, 5));
        mealCombo = new JComboBox<>(mealNames.toArray(new String[0]));
        weightSpinner = new JSpinner(new SpinnerNumberModel(100.0, 1.0, 1000.0, 1.0));

        add(new JLabel("Прием пищи:"));
        add(mealCombo);
        add(new JLabel("Вес (г):"));
        add(weightSpinner);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            confirmed = true;
            dispose();
        });
        JButton cancelButton = new JButton("Отмена");
        cancelButton.addActionListener(e -> dispose());

        add(okButton);
        add(cancelButton);
        pack();
        setLocationRelativeTo(getParent());
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

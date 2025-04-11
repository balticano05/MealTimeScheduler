package org.example.view;

import org.example.entity.User;
import org.example.utils.ErrorHandler;

import javax.swing.*;
import java.awt.*;

public class UserSettingsPanel extends JPanel {

    private JTextField weightField;
    private JTextField heightField;
    private JTextField ageField;
    private JComboBox<User.ActivityLevel> activityCombo;

    public UserSettingsPanel() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridLayout(5, 2));

        add(new JLabel("Вес (кг):"));
        weightField = new JTextField();
        add(weightField);

        add(new JLabel("Рост (см):"));
        heightField = new JTextField();
        add(heightField);

        add(new JLabel("Возраст:"));
        ageField = new JTextField();
        add(ageField);

        add(new JLabel("Активность:"));
        activityCombo = new JComboBox<>(User.ActivityLevel.values());
        add(activityCombo);

        JButton saveButton = new JButton("Сохранить");
        saveButton.addActionListener(e -> saveSettings());
        add(saveButton);
    }

    private void saveSettings() {
        try {

            User user = new User(
                    Double.parseDouble(weightField.getText()),
                    Double.parseDouble(heightField.getText()),
                    Integer.parseInt(ageField.getText()),
                    (User.ActivityLevel) activityCombo.getSelectedItem()
            );

        } catch (NumberFormatException ex) {
            ErrorHandler.handleException(this, ex);
        }
    }

}

package org.example.view;

import org.example.entity.User;
import org.example.repository.UserRepository;
import org.example.service.CalorieService;
import org.example.utils.ErrorHandler;
import org.example.utils.Parser;

import javax.swing.*;
import java.awt.*;

import static org.example.utils.Parser.parseDoubleField;
import static org.example.utils.Parser.parseIntField;

public class UserSettingsPanel extends JPanel {

    private JTextField weightField;
    private JTextField heightField;
    private JTextField ageField;
    private JComboBox<User.ActivityLevel> activityCombo;

    private final UserRepository userRepository;

    public UserSettingsPanel(UserRepository userRepository) {
        this.userRepository = userRepository;
        initComponents();
        loadUserData();
    }

    private void initComponents() {
        setLayout(new GridLayout(5, 2, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

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

    private void loadUserData() {
        User user = userRepository.loadUser();
        weightField.setText(String.format("%.1f", user.getWeight()));
        heightField.setText(String.format("%.1f", user.getHeight()));
        ageField.setText(String.valueOf(user.getAge()));
        activityCombo.setSelectedItem(user.getActivityLevel());
    }

    private void saveSettings() {
        try {
            User user = new User(
                    Parser.parseDoubleField(weightField.getText(), "Вес"),
                    Parser.parseDoubleField(heightField.getText(), "Рост"),
                    Parser.parseIntField(ageField.getText(), "Возраст"),
                    (User.ActivityLevel) activityCombo.getSelectedItem()
            );

            userRepository.saveUser(user);

            double norm = CalorieService.calculateDailyCalorieNorm(user);
            JOptionPane.showMessageDialog(this,
                    "Дневная норма: " + String.format("%.1f ккал", norm),
                    "Норма обновлена",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            ErrorHandler.handleException(this, ex);
        } catch (Exception ex) {
            ErrorHandler.handleException(this,
                    new RuntimeException("Ошибка при сохранении настроек", ex));
        }
    }

}

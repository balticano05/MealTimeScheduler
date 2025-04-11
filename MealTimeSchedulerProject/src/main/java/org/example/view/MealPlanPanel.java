package org.example.view;

import org.example.entity.DailyPlan;
import org.example.entity.Meal;
import org.example.entity.User;
import org.example.service.PlanService;
import org.example.service.ProductService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class MealPlanPanel extends JPanel {

    private final PlanService planService;
    private final ProductService productService;

    public MealPlanPanel(PlanService planService, ProductService productService) {
        this.planService = planService;
        this.productService = productService;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JTabbedPane mealTabs = new JTabbedPane();

        DailyPlan currentPlan = planService.createDailyPlan(LocalDate.now(), getCurrentUser());
        for (Meal meal : currentPlan.getMeals()) {
            mealTabs.addTab(meal.getName(), createMealPanel(meal));
        }

        JPanel infoPanel = new JPanel();
        infoPanel.add(new JLabel("Общая калорийность: " + calculateTotalCalories(currentPlan)));

        add(mealTabs, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.SOUTH);
    }

    private JPanel createMealPanel(Meal meal) {
        return new MealDetailsPanel(meal, productService, planService);
    }

    private double calculateTotalCalories(DailyPlan plan) {
        return plan.getMeals().stream()
                .flatMap(m -> m.getItems().stream())
                .mapToDouble(i -> (i.getProduct().getCalories() * i.getWeight()) / 100)
                .sum();
    }

    private User getCurrentUser() {
        return new User(70, 175, 30, User.ActivityLevel.MEDIUM);
    }

}

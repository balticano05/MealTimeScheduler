package org.example.view;

import org.example.entity.*;
import org.example.service.PlanService;
import org.example.service.ProductService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class MealPlanPanel extends JPanel implements PlanService.PlanChangeListener{

    private final PlanService planService;
    private final ProductService productService;

    public MealPlanPanel(PlanService planService, ProductService productService) {
        this.planService = planService;
        this.productService = productService;
        planService.addPlanChangeListener(this);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JTabbedPane mealTabs = new JTabbedPane();

        DailyPlan currentPlan = planService.getCurrentPlan();

        for (Meal meal : currentPlan.getMeals()) {
            mealTabs.addTab(meal.getName(), createMealPanel(meal));
        }

        NutritionInfo total = calculateTotalNutrition(currentPlan);

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

    private void refreshPlan() {
        removeAll();

        DailyPlan currentPlan = planService.getCurrentPlan();
        JTabbedPane mealTabs = new JTabbedPane();

        currentPlan.getMeals().forEach(meal -> {
            MealDetailsPanel mealPanel = new MealDetailsPanel(meal, productService, planService);
            mealTabs.addTab(meal.getName(), mealPanel);
        });

        NutritionInfo total = calculateTotalNutrition(currentPlan);
        JPanel infoPanel = new JPanel();
        infoPanel.add(new JLabel(String.format("Калории: %.1f", total.getCalories())));
        infoPanel.add(new JLabel(String.format("Белки: %.1fг", total.getProtein())));
        infoPanel.add(new JLabel(String.format("Жиры: %.1fг", total.getFats())));
        infoPanel.add(new JLabel(String.format("Углеводы: %.1fг", total.getCarbs())));

        add(mealTabs, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    private NutritionInfo calculateTotalNutrition(DailyPlan plan) {
        NutritionInfo info = new NutritionInfo();
        plan.getMeals().stream()
                .flatMap(m -> m.getItems().stream())
                .forEach(item -> {
                    Product p = item.getProduct();
                    double weight = item.getWeight();
                    info.addCalories((p.getCalories() * weight) / 100);
                    info.addProtein((parseDouble(p.getProtein()) * weight) / 100);
                    info.addFats((parseDouble(p.getFats()) * weight) / 100);
                    info.addCarbs((parseDouble(p.getCarbs()) * weight) / 100);
                });
        return info;
    }

    private double parseDouble(String value) {
        return Double.parseDouble(value.replace(',', '.'));
    }

    @Override
    public void onPlanChanged() {
        refreshPlan();
    }
}

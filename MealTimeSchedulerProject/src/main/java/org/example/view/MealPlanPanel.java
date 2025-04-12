package org.example.view;

import org.example.entity.*;
import org.example.repository.UserRepository;
import org.example.service.CalorieService;
import org.example.service.PlanService;
import org.example.service.ProductService;

import javax.swing.*;
import java.awt.*;

public class MealPlanPanel extends JPanel implements PlanService.PlanChangeListener{

    private final UserRepository userRepository;;
    private JProgressBar progressBar;
    private final PlanService planService;
    private final ProductService productService;

    public MealPlanPanel(PlanService planService, ProductService productService, UserRepository userRepository) {
        this.userRepository = userRepository;
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
        User user = userRepository.loadUser();

        double dailyNorm = CalorieService.calculateDailyCalorieNorm(user);

        JTabbedPane mealTabs = new JTabbedPane();

        currentPlan.getMeals().forEach(meal -> {
            MealDetailsPanel mealPanel = new MealDetailsPanel(meal, productService, planService);
            mealTabs.addTab(meal.getName(), mealPanel);
        });

        NutritionInfo total = calculateTotalNutrition(currentPlan);

        progressBar = new JProgressBar(0, (int) Math.ceil(dailyNorm));
        progressBar.setValue((int) Math.ceil(total.getCalories()));
        progressBar.setStringPainted(true);
        progressBar.setString(String.format("%.0f/%.0f ккал", total.getCalories(), dailyNorm));

        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        JPanel nutritionPanel = new JPanel();

        nutritionPanel.add(new JLabel(String.format("Калории: %.1f/%.1f", total.getCalories(), dailyNorm)));
        nutritionPanel.add(new JLabel(String.format("Белки: %.1fг", total.getProtein())));
        nutritionPanel.add(new JLabel(String.format("Жиры: %.1fг", total.getFats())));
        nutritionPanel.add(new JLabel(String.format("Углеводы: %.1fг", total.getCarbs())));

        infoPanel.add(nutritionPanel);
        infoPanel.add(progressBar);

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

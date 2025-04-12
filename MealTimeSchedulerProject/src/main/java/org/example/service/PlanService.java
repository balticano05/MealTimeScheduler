package org.example.service;

import lombok.Getter;
import org.example.entity.DailyPlan;
import org.example.entity.Meal;
import org.example.entity.MealItem;
import org.example.entity.Product;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PlanService {

    private static final String DAILY_PLANS_DIR = "src/main/resources/daily_plans/";

    private final ProductService productService;
    private final DailyPlan currentPlan;
    private final List<PlanChangeListener> listeners = new ArrayList<>();

    public PlanService(ProductService productService) {

        this.productService = productService;
        this.currentPlan = createDefaultPlan();

        currentPlan.setMeals(new ArrayList<>(Arrays.asList(
                new Meal("Завтрак"),
                new Meal("Обед"),
                new Meal("Ужин")
        )));
    }

    private DailyPlan createDefaultPlan() {
        DailyPlan plan = new DailyPlan();
        plan.setDate(LocalDate.now());
        plan.setMeals(createDefaultMeals());
        return plan;
    }

    private List<Meal> createDefaultMeals() {
        return Arrays.asList(
                new Meal("Завтрак"),
                new Meal("Обед"),
                new Meal("Ужин")
        );
    }

    public void addPlanChangeListener(PlanChangeListener listener) {
        listeners.add(listener);
    }

    private void firePlanChanged() {
        listeners.forEach(PlanChangeListener::onPlanChanged);
    }

    public List<String> getMealNames() {
        return currentPlan.getMeals().stream()
                .map(Meal::getName)
                .collect(Collectors.toList());
    }

    public void addProductToMeal(String mealName, Product product, double weight) {
        currentPlan.getMeals().stream()
                .filter(m -> m.getName().equals(mealName))
                .findFirst()
                .ifPresent(meal -> {
                    meal.addItem(new MealItem(product, weight));
                    firePlanChanged();
                });
    }

    public interface PlanChangeListener {
        void onPlanChanged();
    }

}

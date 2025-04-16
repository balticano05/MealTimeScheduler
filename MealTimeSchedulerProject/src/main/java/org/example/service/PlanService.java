package org.example.service;

import lombok.Getter;
import org.example.entity.DailyPlan;
import org.example.entity.Meal;
import org.example.entity.MealItem;
import org.example.entity.Product;
import org.example.utils.XmlUtils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.example.utils.XmlUtils.serializeToXml;

@Getter
public class PlanService {

    private static final String PLAN_PATH = "src/main/resources/current_plan.xml";

    private final ProductService productService;
    private final DailyPlan currentPlan;
    private final List<PlanChangeListener> listeners = new ArrayList<>();

    public PlanService(ProductService productService) {
        this.productService = productService;
        this.currentPlan = loadPlan();

        System.out.println("Deserialize:"+PLAN_PATH);


        if(currentPlan.getMeals().isEmpty()) {
            currentPlan.setMeals(createDefaultMeals());
        }
    }

    private DailyPlan loadPlan() {

        try {
            return XmlUtils.deserializeFromXml(PLAN_PATH, DailyPlan.class);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    public void savePlan() {
        try {
            serializeToXml(currentPlan, PLAN_PATH);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save plan", e);
        }
    }

    private DailyPlan createDefaultPlan() {
        DailyPlan plan = new DailyPlan();
        plan.setDate(LocalDate.now());
        plan.setMeals(createDefaultMeals());
        return plan;
    }

    private List<Meal> createDefaultMeals() {
        return Arrays.asList(
                new Meal("Завтрак", new ArrayList<>()),
                new Meal("Обед", new ArrayList<>()),
                new Meal("Ужин", new ArrayList<>())
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
                    MealItem item = new MealItem();
                    item.setProduct(product);
                    item.setWeight(weight);
                    meal.getItems().add(item);
                    savePlan();
                    firePlanChanged();
                });
    }

    public interface PlanChangeListener {
        void onPlanChanged();
    }

}

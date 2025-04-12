package org.example.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.example.entity.*;
import org.example.utils.XmlUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PlanService {

    private DailyPlan currentPlan;
    private List<PlanChangeListener> listeners = new ArrayList<>();

    private static final String DAILY_PLANS_DIR = "src/main/resources/daily_plans/";

    public enum ExportFormat {
        PDF, DOC, XML
    }

    private final ProductService productService;

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

    public DailyPlan createDailyPlan(LocalDate date, User user) {
        DailyPlan plan = new DailyPlan();
        plan.setDate(date);
        plan.setDailyCalorieNorm(CalorieService.calculateDailyCalorieNorm(user));

        plan.getMeals().add(new Meal("Завтрак"));
        plan.getMeals().add(new Meal("Обед"));
        plan.getMeals().add(new Meal("Ужин"));

        return plan;
    }

    public void addProductToMeal(Meal meal, Product product, double weight) {
        meal.getItems().add(new MealItem(product, weight));
    }

    public void updateProductWeightInMeal(Meal meal, String productName, double newWeight) {
        meal.getItems().stream()
                .filter(item -> item.getProduct().getName().equals(productName))
                .findFirst()
                .ifPresent(item -> item.setWeight(newWeight));
    }

    public void removeProductFromMeal(Meal meal, String productName) {
        meal.getItems().removeIf(item -> item.getProduct().getName().equals(productName));
    }

    public void exportPlan(DailyPlan plan, ExportFormat format, String filePath) {
        switch (format) {
            case PDF -> exportToPdf(plan, filePath);
            case DOC -> exportToDoc(plan, filePath);
            case XML -> exportToXml(plan, filePath);
        }
    }

    private void exportToPdf(DailyPlan plan, String filePath) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(100, 700);
                contentStream.showText("Daily Nutrition Plan - " + plan.getDate());
                contentStream.endText();
            }
            document.save(filePath);
        } catch (IOException e) {
            throw new RuntimeException("PDF export failed", e);
        }
    }

    private void exportToDoc(DailyPlan plan, String filePath) {
        try (XWPFDocument document = new XWPFDocument()) {
            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();
            run.setText("Daily Nutrition Plan - " + plan.getDate());
            document.write(new FileOutputStream(filePath));
        } catch (IOException e) {
            throw new RuntimeException("DOC export failed", e);
        }
    }

    private void exportToXml(DailyPlan plan, String filePath) {
        try {
            // Создаем директорию если не существует
            new File(DAILY_PLANS_DIR).mkdirs();

            String fullPath = DAILY_PLANS_DIR + filePath;
            XmlUtils.serializeToXml(DailyPlan.class, fullPath);
        } catch (IOException e) {
            throw new RuntimeException("XML export failed", e);
        }
    }

    public void addPlanChangeListener(PlanChangeListener listener) {
        listeners.add(listener);
    }

    private void firePlanChanged() {
        listeners.forEach(PlanChangeListener::onPlanChanged);
    }

    public interface PlanChangeListener {
        void onPlanChanged();
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
                    // Используем новый метод addItem вместо прямого доступа к списку
                    meal.addItem(new MealItem(product, weight));
                    firePlanChanged();
                });
    }

}

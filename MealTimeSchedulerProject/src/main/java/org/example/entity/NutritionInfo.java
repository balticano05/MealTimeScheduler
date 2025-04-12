package org.example.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NutritionInfo {

    private double calories;
    private double protein;
    private double fats;
    private double carbs;

    public void addCalories(double calories) {
        this.calories += calories;
    }

    public void addProtein(double protein) {
        this.protein += protein;
    }

    public void addFats(double fats) {
        this.fats += fats;
    }

    public void addCarbs(double carbs) {
        this.carbs += carbs;
    }

}
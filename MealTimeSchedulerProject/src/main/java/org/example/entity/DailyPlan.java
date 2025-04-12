package org.example.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class DailyPlan {

    private LocalDate date;
    private List<Meal> meals = new ArrayList<>();
    private double dailyCalorieNorm;

}
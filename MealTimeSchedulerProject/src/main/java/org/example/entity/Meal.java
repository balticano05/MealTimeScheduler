package org.example.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Meal {
    private String name;
    private List<MealItem> items = new ArrayList<>();

    public Meal(String name) {
        this.name = name;
    }
}
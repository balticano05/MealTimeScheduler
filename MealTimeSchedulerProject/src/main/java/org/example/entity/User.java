package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private double weight;
    private double height;
    private int age;
    private ActivityLevel activityLevel;

    public enum ActivityLevel {
        LOW, NORMAL, MEDIUM, HIGH
    }
}
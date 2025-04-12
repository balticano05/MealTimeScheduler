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

        LOW("Низкая"),
        NORMAL("Нормальная"),
        MEDIUM("Средняя"),
        HIGH("Высокая");

        private final String displayName;

        ActivityLevel(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }

    }

}
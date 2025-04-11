package org.example.service;

import org.example.entity.User;

public class CalorieService {

    public static double calculateDailyCalorieNorm(User user) {
        double bmr = 447.593 + (9.247 * user.getWeight()) +
                (3.098 * user.getHeight()) - (4.330 * user.getAge());

        double arm = switch(user.getActivityLevel()) {
            case LOW -> 1.2;
            case NORMAL -> 1.375;
            case MEDIUM -> 1.55;
            case HIGH -> 1.725;
        };

        return bmr * arm;
    }

}

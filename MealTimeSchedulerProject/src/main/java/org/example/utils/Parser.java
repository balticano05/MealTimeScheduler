package org.example.utils;

import java.util.Locale;

public class Parser {

    public static double parseDoubleField(String value, String fieldName) throws NumberFormatException {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new NumberFormatException(fieldName + " должен быть числом");
        }
    }

    public static int parseIntField(String value, String fieldName) throws NumberFormatException {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new NumberFormatException(fieldName + " должен быть целым числом");
        }
    }

    public static double parseNutritionValue(String value) {
        return Double.parseDouble(value.replace(',', '.').trim());
    }

    public static String formatDoubleValue(Object value) {
        return String.format(Locale.US, "%.2f", value);
    }

}

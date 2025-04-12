package org.example.view;

import org.example.entity.MealItem;
import org.example.utils.Parser;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class MealProductsTableModel extends AbstractTableModel {

    private final List<MealItem> items;
    private final String[] columns = {"Продукт", "Вес (г)", "Калории", "Белки", "Жиры", "Углеводы"};

    public MealProductsTableModel(List<MealItem> items) {
        this.items = items;
    }

    @Override
    public int getRowCount() {
        return items.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public Object getValueAt(int row, int column) {
        MealItem item = items.get(row);
        return switch (column) {
            case 0 -> item.getProduct().getName();
            case 1 -> item.getWeight();
            case 2 -> (item.getProduct().getCalories() * item.getWeight()) / 100;
            case 3 -> Parser.parseNutritionValue(item.getProduct().getProtein()) * item.getWeight() / 100;
            case 4 -> Parser.parseNutritionValue(item.getProduct().getFats()) * item.getWeight() / 100;
            case 5 -> Parser.parseNutritionValue(item.getProduct().getCarbs()) * item.getWeight() / 100;
            default -> null;
        };
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnIndex == 0 ? String.class : Double.class;
    }

}

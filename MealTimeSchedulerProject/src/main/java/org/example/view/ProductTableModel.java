package org.example.view;

import org.example.entity.Product;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class ProductTableModel extends AbstractTableModel {

    private final List<Product> products;
    private final String[] columns = {"Название", "Калории", "Белки", "Жиры", "Углеводы"};

    public ProductTableModel(List<Product> products) {
        this.products = products;
    }

    @Override
    public int getRowCount() { return products.size(); }

    @Override
    public int getColumnCount() { return columns.length; }

    @Override
    public Object getValueAt(int row, int column) {
        Product p = products.get(row);
        return switch (column) {
            case 0 -> p.getName();
            case 1 -> p.getCalories();
            case 2 -> p.getProtein();
            case 3 -> p.getFats();
            case 4 -> p.getCarbs();
            default -> null;
        };
    }

    public Product getProductAt(int selectedRow) {
        return products.get(selectedRow);
    }

}

package org.example.view;

import javax.swing.*;
import java.awt.*;

class SpinnerEditor extends DefaultCellEditor {
    private JSpinner spinner;

    public SpinnerEditor() {
        super(new JTextField());
        spinner = new JSpinner(new SpinnerNumberModel(100.0, 1.0, 1000.0, 1.0));
        JFormattedTextField tf = ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField();
        tf.setEditable(true);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        spinner.setValue(value);
        return spinner;
    }

    @Override
    public Object getCellEditorValue() {
        return spinner.getValue();
    }
}
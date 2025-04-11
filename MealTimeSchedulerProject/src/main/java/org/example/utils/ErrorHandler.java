package org.example.utils;

import javax.swing.*;
import java.awt.*;

public class ErrorHandler {
    public static void handleException(Component parent, Exception e) {
        JOptionPane.showMessageDialog(parent,
                "Error: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }
}
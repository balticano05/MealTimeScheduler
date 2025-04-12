package org.example;

import org.example.entity.Db;
import org.example.repository.DbRepository;
import org.example.repository.UserRepository;
import org.example.service.PlanService;
import org.example.service.ProductService;
import org.example.utils.ErrorHandler;
import org.example.utils.XmlUtils;
import org.example.view.MainFrame;

import javax.swing.*;
import java.io.IOException;

public class AppRunner {

    public static void runMealTimeScheduler(){

        SwingUtilities.invokeLater(() -> {
            try {
                UserRepository userRepository = new UserRepository();
                DbRepository dbRepository = new DbRepository("src/main/resources/food-data.xml");
                ProductService productService = new ProductService(dbRepository);
                PlanService planService = new PlanService(productService);

                MainFrame mainFrame = new MainFrame(productService, planService, userRepository);
                mainFrame.setVisible(true);

            } catch (Exception e) {
                ErrorHandler.handleException(null, e);
            }
        });
    }
}
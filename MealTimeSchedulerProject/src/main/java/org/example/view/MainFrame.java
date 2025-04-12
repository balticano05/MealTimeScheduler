package org.example.view;

import org.example.repository.UserRepository;
import org.example.service.PlanService;
import org.example.service.ProductService;

import javax.swing.*;

public class MainFrame extends JFrame {

    private final UserRepository userRepository;
    private final ProductService productService;
    private final PlanService planService;

    public MainFrame(ProductService productService, PlanService planService, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.productService = productService;
        this.planService = planService;
        initializeUI();

    }

    private void initializeUI() {

        setTitle("Планировщик питания");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Каталог продуктов", new ProductCatalogPanel(productService, planService));

        tabbedPane.addTab("План питания",
                new MealPlanPanel(planService, productService, userRepository));
        tabbedPane.addTab("Настройки пользователя", new UserSettingsPanel(userRepository));

        add(tabbedPane);

    }

}

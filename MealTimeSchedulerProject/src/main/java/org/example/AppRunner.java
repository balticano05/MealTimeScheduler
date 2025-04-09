package org.example;

import org.example.entity.Db;
import org.example.utils.XmlUtils;

import java.io.IOException;

public class AppRunner {

    public static void runMealTimeScheduler(){

        try {

            Db db = XmlUtils.deserializeFromXml("src/main/resources/food-data.xml");
            System.out.println("Успешно загружено категорий: " + db.getCategories().size());

            System.out.println(db.getCategories().getFirst().getDescription());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
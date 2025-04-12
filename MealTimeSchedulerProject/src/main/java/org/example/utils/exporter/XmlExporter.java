package org.example.utils.exporter;

import org.example.entity.DailyPlan;
import org.example.utils.XmlUtils;

import java.io.IOException;

public class XmlExporter {

    public static void export(DailyPlan plan, String filename) throws IOException {
        XmlUtils.serializeToXml(plan, filename);
    }

}

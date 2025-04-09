package org.example.utils;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.example.entity.Db;

import java.io.File;
import java.io.IOException;

public class XmlUtils {

    private static final XmlMapper xmlMapper = new XmlMapper();

    public static Db deserializeFromXml(String filePath) throws IOException {
        return xmlMapper.readValue(new File(filePath), Db.class);
    }

    public static void serializeToXml(Db db, String outputPath) throws IOException {
        xmlMapper.writeValue(new File(outputPath), db);
    }

}
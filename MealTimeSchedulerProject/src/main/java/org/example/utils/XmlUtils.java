package org.example.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.entity.DailyPlan;
import org.example.entity.Db;

import java.io.File;
import java.io.IOException;

public final class XmlUtils {

    private static final XmlMapper XML_MAPPER = new XmlMapper();


    static {
        XML_MAPPER.configure(SerializationFeature.INDENT_OUTPUT, true);
        XML_MAPPER.registerModule(new JavaTimeModule());
        XML_MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        XML_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static <T> T deserializeFromXml(String filePath, Class<T> valueType) throws IOException {
        try {
            return XML_MAPPER.readValue(new File(filePath), valueType);
        } catch (IOException e) {
            throw new IOException("Failed to deserialize XML from: " + filePath, e);
        }
    }

    public static void serializeToXml(Object object, String outputPath) throws IOException {
        try {
            XML_MAPPER.writeValue(new File(outputPath), object);
        } catch (IOException e) {
            throw new IOException("Failed to serialize object to: " + outputPath, e);
        }
    }

}

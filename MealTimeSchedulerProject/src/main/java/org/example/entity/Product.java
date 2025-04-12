package org.example.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.utils.CommaDoubleDeserializer;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @JacksonXmlProperty(localName = "Name")
    private String name;

    @JacksonXmlProperty(localName = "Gramms")
    private double gramms;

    @JacksonXmlProperty(localName = "Protein")
    private String protein;

    @JacksonXmlProperty(localName = "Fats")
    private String fats;

    @JacksonXmlProperty(localName = "Carbs")
    private String carbs;

    @JsonDeserialize(using = CommaDoubleDeserializer.class)
    @JacksonXmlProperty(localName = "Calories")
    private double calories;

    public void setProtein(String protein) {
        this.protein = protein.replace(',', '.');
    }

}
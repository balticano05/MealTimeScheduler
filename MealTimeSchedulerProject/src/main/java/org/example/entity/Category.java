package org.example.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @JacksonXmlProperty(isAttribute = true, localName = "name")
    private String name;

    @JacksonXmlProperty(isAttribute = true, localName = "description")
    private String description;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "Product")
    private List<Product> products;

}
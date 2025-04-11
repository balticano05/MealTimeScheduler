package org.example.entity;

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
public class Db {

    @JacksonXmlProperty(localName = "Category")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Category> categories;

    public void addCategory(Category category) {
        categories.add(category);
    }

}
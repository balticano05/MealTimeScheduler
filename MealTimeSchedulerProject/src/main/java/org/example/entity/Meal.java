package org.example.entity;

import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@XmlRootElement(name = "meal")
@XmlAccessorType(XmlAccessType.FIELD)
@AllArgsConstructor
@NoArgsConstructor
public class Meal {
    @XmlElement(name = "name")
    private String name;

    @XmlElementWrapper(name = "items")
    @XmlElement(name = "item")
    private List<MealItem> items = new ArrayList<>();
}
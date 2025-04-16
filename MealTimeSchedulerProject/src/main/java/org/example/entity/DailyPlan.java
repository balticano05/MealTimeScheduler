package org.example.entity;

import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@XmlRootElement(name = "DailyPlan")
@XmlAccessorType(XmlAccessType.FIELD)
public class DailyPlan {
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    @XmlElement(name = "date")
    private LocalDate date;

    @XmlElementWrapper(name = "meals")
    @XmlElement(name = "meal")
    private List<Meal> meals = new ArrayList<>();

}

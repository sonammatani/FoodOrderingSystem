package com.project.model;

import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.Data;
import java.util.List;

@Entity
@Data
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private double rating;
    private int maxCapacity;
    private int currentCapacity;  // To keep track of current load

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<MenuItem> menuItems;
}



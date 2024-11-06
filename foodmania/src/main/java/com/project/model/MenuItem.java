package com.project.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Getter
@Setter
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private double price;
    private int preparationTime;

    @ManyToOne
    private Restaurant restaurant;
}

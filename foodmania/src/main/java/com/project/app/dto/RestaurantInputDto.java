package com.project.app.dto;

import com.project.app.model.MenuItem;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class RestaurantInputDto {

    private Long id;
    private String name;
    private double rating;
    private int maxCapacity;
    private int currentCapacity;
    private List<MenuItem> menuItems;

}

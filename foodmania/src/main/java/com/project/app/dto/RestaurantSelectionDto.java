package com.project.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantSelectionDto {

    private String restaurantName;
    private Long restaurantId;
    private String itemName;
    private Double itemPrice;
}

package com.project.app.factory;

import com.project.app.javainterface.RestaurantSelectionStrategy;
import com.project.app.exception.InvalidStrategyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RestaurantSelectionStrategyFactory {

    @Value("${order.selection.strategy}")
    private String strategy;

    @Autowired
    private RestaurantSelectionStrategy lowestCostStrategy;

    /**
     * selects strategy based on the input provided.
     *
     * @param selectedStrategy selectedStrategy
     * @return selected strategy implementation class
     */
    public RestaurantSelectionStrategy getSelectionStrategy(String selectedStrategy) {
        if (selectedStrategy == null) {
            throw new IllegalArgumentException("Selected strategy cannot be null");
        }
        if (selectedStrategy.equalsIgnoreCase(strategy)) {
            return lowestCostStrategy;
        } else {
            throw new InvalidStrategyException("Invalid strategy: " + selectedStrategy);
        }
    }
}

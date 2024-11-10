package com.project.app.factory;

import com.project.app.javainterface.RestaurantSelectionStrategy;
import com.project.app.exception.InvalidStrategyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantSelectionStrategyFactoryTest {

    @InjectMocks
    private RestaurantSelectionStrategyFactory factory;

    @Mock
    private RestaurantSelectionStrategy lowestCostStrategy;

    @BeforeEach
    void setUp() {
        factory = new RestaurantSelectionStrategyFactory();
        ReflectionTestUtils.setField(factory, "strategy", "lowestCost");
    }

    @Test
    void testGetSelectionStrategy_LowestCostStrategy() {
        RestaurantSelectionStrategy result = factory.getSelectionStrategy("lowestCost");
        assertEquals(lowestCostStrategy, result);
    }

    @Test
    void testGetSelectionStrategy_InvalidStrategy() {
        assertThrows(InvalidStrategyException.class, () -> factory.getSelectionStrategy("invalidStrategy"));
    }
}

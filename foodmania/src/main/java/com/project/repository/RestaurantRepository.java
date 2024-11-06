package com.project.repository;

import com.project.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    @Query("SELECT r FROM Restaurant r JOIN r.menuItems m WHERE m.name = :itemName AND r.currentCapacity < r.maxCapacity")
    List<Restaurant> findAvailableRestaurantsForItem(@Param("itemName") String itemName);
}


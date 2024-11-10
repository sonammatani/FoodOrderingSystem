package com.project.app.repository;

import com.project.app.dto.RestaurantSelectionDto;
import com.project.app.model.Restaurant;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    @Query("SELECT r FROM Restaurant r WHERE r.currentCapacity < r.maxCapacity " +
            "AND r.maxCapacity - r.currentCapacity  >= :quantity " +
            "AND EXISTS (SELECT m FROM MenuItem m WHERE m.name = :itemName AND m.restaurantId = r.id)")
    List<Restaurant> findAvailableRestaurantsWithItem(@Param("itemName") String itemName,
                                                      @Param("quantity") int quantity);

    @Query("Select new com.project.app.dto.RestaurantSelectionDto(r.name, r.id, m.name , m.price) from Restaurant r " +
            "Join MenuItem m on m.restaurantId IN (:restaurantIds) and m.name = :itemName")
    List<RestaurantSelectionDto> findDataForEligibleRestaurants(@Param("restaurantIds") List<Long> restaurantIds,
                                                                @Param("itemName") String itemName);

    Restaurant findByName(String restaurantName);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("Select r from Restaurant r where r.id in (:id)")
    List<Restaurant> findAllByRestaurantIds(List<Long> id);


}




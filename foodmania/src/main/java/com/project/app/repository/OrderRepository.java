package com.project.app.repository;

import com.project.app.model.OrderData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderData, Long> {
}

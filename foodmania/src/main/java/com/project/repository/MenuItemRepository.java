package com.project.repository;
import com.project.model.MenuItem;
import com.project.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

}

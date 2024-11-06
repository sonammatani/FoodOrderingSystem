package com.project.repository;
import com.project.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    MenuItem findByName(String name);
}

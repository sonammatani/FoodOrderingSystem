package com.project.repository;
import com.project.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    MenuItem findByName(String name);

    @Query("SELECT DISTINCT m.name FROM MenuItem m")
    List<String> findAllUniqueMenuItems();
}

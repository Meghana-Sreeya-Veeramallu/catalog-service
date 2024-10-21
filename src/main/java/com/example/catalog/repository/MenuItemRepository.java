package com.example.catalog.repository;

import com.example.catalog.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    @Query("SELECT m FROM MenuItem m WHERE m.name = :itemName AND m.restaurant.id = :restaurantId")
    Optional<MenuItem> findByNameAndRestaurantId(String itemName, Long restaurantId);
}
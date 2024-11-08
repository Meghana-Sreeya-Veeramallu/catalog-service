package com.example.catalog.repository;

import com.example.catalog.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    @Query("SELECT r FROM Restaurant r WHERE r.name = :name AND r.address = :address")
    Optional<Restaurant> findByNameAndAddress(String name, String address);
}

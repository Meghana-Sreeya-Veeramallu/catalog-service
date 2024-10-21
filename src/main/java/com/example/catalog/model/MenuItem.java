package com.example.catalog.model;

import com.example.catalog.Exceptions.MenuItemNameCannotBeNullOrEmptyException;
import com.example.catalog.Exceptions.PriceMustBePositiveException;
import jakarta.persistence.*;

@Entity
@Table(name = "menuitems")
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private double price;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    public MenuItem() {}

    public MenuItem(String name, double price) {
        if (name == null || name.trim().isEmpty()) {
            throw new MenuItemNameCannotBeNullOrEmptyException("Menu item name cannot be null or empty");
        }
        if (price <= 0) {
            throw new PriceMustBePositiveException("Price must be positive");
        }
        this.name = name;
        this.price = price;
    }
}

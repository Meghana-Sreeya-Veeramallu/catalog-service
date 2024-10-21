package com.example.catalog.model;

import com.example.catalog.Exceptions.MenuItemNameCannotBeNullOrEmptyException;
import com.example.catalog.Exceptions.PriceMustBePositiveException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Entity
@Table(name = "menuItems")
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;
    @Getter
    private String name;
    @Getter
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

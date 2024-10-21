package com.example.catalog.model;

import com.example.catalog.Exceptions.RestaurantAddressCannotBeNullOrEmptyException;
import com.example.catalog.Exceptions.RestaurantNameCannotBeNullOrEmptyException;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "restaurants")
public class Restaurant {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String address;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private final List<MenuItem> menuItems = new ArrayList<>();

    public Restaurant() {}

    public Restaurant(String name, String address) {
        if (name == null || name.trim().isEmpty()) {
            throw new RestaurantNameCannotBeNullOrEmptyException("Restaurant name cannot be null or empty");
        }
        if (address == null || address.trim().isEmpty()) {
            throw new RestaurantAddressCannotBeNullOrEmptyException("Restaurant address cannot be null or empty");
        }
        this.name = name;
        this.address = address;
    }

    public MenuItem addMenuItem(String name, double price) {
        MenuItem menuItem = new MenuItem(name, price);
        menuItem.setRestaurant(this);
        menuItems.add(menuItem);
        return menuItem;
    }
}

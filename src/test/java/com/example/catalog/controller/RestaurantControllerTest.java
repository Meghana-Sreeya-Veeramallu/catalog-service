package com.example.catalog.controller;

import com.example.catalog.Dto.RestaurantDto;
import com.example.catalog.Exceptions.*;
import com.example.catalog.model.Restaurant;
import com.example.catalog.service.RestaurantService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RestaurantControllerTest {

    @InjectMocks
    private RestaurantController restaurantController;

    @Mock
    private RestaurantService restaurantService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(restaurantController).setControllerAdvice(new GlobalExceptionHandler()).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testAddRestaurantSuccessfully() throws Exception {
        RestaurantDto restaurantDto = new RestaurantDto("Burger King", "Hyderabad");
        String jsonRequestBody = objectMapper.writeValueAsString(restaurantDto);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/catalog/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        assertEquals("Restaurant added successfully: Burger King at address: Hyderabad", responseBody);
        verify(restaurantService, times(1)).addRestaurant("Burger King", "Hyderabad");
    }

    @Test
    void testAddRestaurantWithNullName() throws Exception {
        RestaurantDto restaurantDto = new RestaurantDto(null, "Hyderabad");
        String jsonRequestBody = objectMapper.writeValueAsString(restaurantDto);

        doThrow(new RestaurantNameCannotBeNullOrEmptyException("Restaurant name cannot be null or empty")).when(restaurantService).addRestaurant(null, "Hyderabad");

        mockMvc.perform(MockMvcRequestBuilders.post("/catalog/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request: Restaurant name cannot be null or empty"));

        verify(restaurantService, times(1)).addRestaurant(any(), any());
    }

    @Test
    void testAddRestaurantWithNullAddress() throws Exception {
        RestaurantDto restaurantDto = new RestaurantDto("Burger King", null);
        String jsonRequestBody = objectMapper.writeValueAsString(restaurantDto);

        doThrow(new RestaurantAddressCannotBeNullOrEmptyException("Restaurant address cannot be null or empty")).when(restaurantService).addRestaurant("Burger King", null);

        mockMvc.perform(MockMvcRequestBuilders.post("/catalog/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request: Restaurant address cannot be null or empty"));

        verify(restaurantService, times(1)).addRestaurant(any(), any());
    }

    @Test
    void testAddRestaurantAlreadyExists() throws Exception {
        RestaurantDto restaurantDto = new RestaurantDto("Burger King", "Hyderabad");
        String jsonRequestBody = objectMapper.writeValueAsString(restaurantDto);

        doThrow(new RestaurantAlreadyExistsException("Restaurant with name 'Burger King' and address 'Hyderabad' already exists"))
                .when(restaurantService).addRestaurant("Burger King", "Hyderabad");

        mockMvc.perform(MockMvcRequestBuilders.post("/catalog/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isConflict())
                .andExpect(content().string("Conflict: Restaurant with name 'Burger King' and address 'Hyderabad' already exists"));

        verify(restaurantService, times(1)).addRestaurant("Burger King", "Hyderabad");
    }

    @Test
    void testGetAllRestaurants() throws Exception {
        List<Restaurant> restaurants = Arrays.asList(
                new Restaurant("Burger King", "Hyderabad"),
                new Restaurant("Pizza Hut", "Bengaluru")
        );

        when(restaurantService.getAllRestaurants()).thenReturn(restaurants);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/catalog/restaurants")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        assertEquals(objectMapper.writeValueAsString(restaurants), responseBody);
        assertEquals(2, restaurants.size());
    }

    @Test
    void testGetAllRestaurantsEmpty() throws Exception {
        when(restaurantService.getAllRestaurants()).thenReturn(Arrays.asList());

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/catalog/restaurants")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        assertEquals("[]", responseBody);
        assertEquals(0, objectMapper.readTree(responseBody).size());

        verify(restaurantService, times(1)).getAllRestaurants();
    }

    @Test
    void testGetRestaurantByIdFound() throws Exception {
        Long restaurantId = 1L;
        Restaurant restaurant = new Restaurant("Burger King", "Hyderabad");

        when(restaurantService.getRestaurantById(restaurantId)).thenReturn(restaurant);

        mockMvc.perform(MockMvcRequestBuilders.get("/catalog/restaurants/{restaurantId}", restaurantId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(restaurant)));

        verify(restaurantService, times(1)).getRestaurantById(restaurantId);
    }

    @Test
    void testGetRestaurantByIdNotFound() throws Exception {
        Long restaurantId = 999L;

        when(restaurantService.getRestaurantById(restaurantId)).thenThrow(new RestaurantNotFoundException("Restaurant with id '999' not found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/catalog/restaurants/{restaurantId}", restaurantId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Not Found: Restaurant with id '999' not found"));

        verify(restaurantService, times(1)).getRestaurantById(restaurantId);
    }
}

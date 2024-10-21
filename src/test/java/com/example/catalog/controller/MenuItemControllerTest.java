package com.example.catalog.controller;

import com.example.catalog.Dto.MenuItemDto;
import com.example.catalog.Exceptions.*;
import com.example.catalog.service.MenuItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MenuItemControllerTest {

    @InjectMocks
    private MenuItemController menuItemController;

    @Mock
    private MenuItemService menuItemService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(menuItemController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testAddMenuItemSuccessfully() throws Exception {
        MenuItemDto menuItemDto = new MenuItemDto("Pasta", 199.0);
        String jsonRequestBody = objectMapper.writeValueAsString(menuItemDto);
        Long restaurantId = 1L;

        MvcResult mvcResult = mockMvc.perform(post("/catalog/restaurants/{restaurantId}/menuItems", restaurantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        assertEquals("Menu item added successfully: Pasta with price: 199.0", responseBody);
        verify(menuItemService, times(1)).addMenuItem(restaurantId, "Pasta", 199);
    }

    @Test
    void testAddMenuItemWithNullName() throws Exception {
        MenuItemDto menuItemDto = new MenuItemDto(null, 199.0);
        String jsonRequestBody = objectMapper.writeValueAsString(menuItemDto);
        Long restaurantId = 1L;

        doThrow(new MenuItemNameCannotBeNullOrEmptyException("Menu item name cannot be null or empty"))
                .when(menuItemService).addMenuItem(restaurantId, null, 199.0);

        mockMvc.perform(post("/catalog/restaurants/{restaurantId}/menuItems", restaurantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request: Menu item name cannot be null or empty"));

        verify(menuItemService, times(1)).addMenuItem(anyLong(), any(), anyDouble());
    }

    @Test
    void testAddMenuItemWithZeroPrice() throws Exception {
        MenuItemDto menuItemDto = new MenuItemDto("Pasta", 0.0);
        String jsonRequestBody = objectMapper.writeValueAsString(menuItemDto);
        Long restaurantId = 1L;

        doThrow(new PriceMustBePositiveException("Price must be positive"))
                .when(menuItemService).addMenuItem(restaurantId, "Pasta", 0.0);

        mockMvc.perform(post("/catalog/restaurants/{restaurantId}/menuItems", restaurantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request: Price must be positive"));

        verify(menuItemService, times(1)).addMenuItem(anyLong(), any(), anyDouble());
    }

    @Test
    void testAddMenuItemWithNegativePrice() throws Exception {
        MenuItemDto menuItemDto = new MenuItemDto("Pasta", -100.0);
        String jsonRequestBody = objectMapper.writeValueAsString(menuItemDto);
        Long restaurantId = 1L;

        doThrow(new PriceMustBePositiveException("Price must be positive"))
                .when(menuItemService).addMenuItem(restaurantId, "Pasta", -100.0);

        mockMvc.perform(post("/catalog/restaurants/{restaurantId}/menuItems", restaurantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request: Price must be positive"));

        verify(menuItemService, times(1)).addMenuItem(anyLong(), any(), anyDouble());
    }

    @Test
    void testAddMenuItemRestaurantNotFound() throws Exception {
        MenuItemDto menuItemDto = new MenuItemDto("Pasta", 199.0);
        String jsonRequestBody = objectMapper.writeValueAsString(menuItemDto);
        Long restaurantId = 999L;

        doThrow(new RestaurantNotFoundException("Restaurant with ID '999' not found"))
                .when(menuItemService).addMenuItem(restaurantId, "Pasta", 199);

        mockMvc.perform(post("/catalog/restaurants/{restaurantId}/menuItems", restaurantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Not Found: Restaurant with ID '999' not found"));

        verify(menuItemService, times(1)).addMenuItem(restaurantId, "Pasta", 199);
    }

    @Test
    void testAddMenuItemAlreadyExists() throws Exception {
        MenuItemDto menuItemDto = new MenuItemDto("Pasta", 199.0);
        String jsonRequestBody = objectMapper.writeValueAsString(menuItemDto);
        Long restaurantId = 1L;

        doThrow(new MenuItemAlreadyExistsException("Menu item 'Pasta' already exists for restaurant with ID '1'"))
                .when(menuItemService).addMenuItem(restaurantId, "Pasta", 199.0);

        mockMvc.perform(post("/catalog/restaurants/{restaurantId}/menuItems", restaurantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isConflict())
                .andExpect(content().string("Conflict: Menu item 'Pasta' already exists for restaurant with ID '1'"));

        verify(menuItemService, times(1)).addMenuItem(restaurantId, "Pasta", 199);
    }
}

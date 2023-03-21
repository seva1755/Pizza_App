package com.example.pizza.controllerTest;

import com.example.pizza.entity.Pizza;
import com.example.pizza.entity.Cafe;
import com.example.pizza.entity.Pizza;
import com.example.pizza.repository.CafeRepository;
import com.example.pizza.repository.PizzaRepository;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PizzaControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    CafeRepository cafeRepository;

    @MockBean
    PizzaRepository pizzaRepository;

    @Autowired
    ObjectMapper objectMapper;

    Cafe cafe;

    /**
     * Для создания пицц необходимо хотя бы одно кафе
     * Новое кафе создается перед каждым тестом
     */
    @BeforeEach
    void init(){
        cafe = new Cafe(1L, "ParisCafe", "Paris",
                "Street2", "paris_cafe@gmail.com", "1234567", new HashSet<>());
        cafeRepository.save(cafe);
    }

    /**
     * Получение списка всех пицц в базе
     * @throws Exception
     */
    @Test
    void getAllPizzas() throws Exception {
        mockMvc.perform(get("/pizzas"))
                .andExpect(status().isOk());
    }

    /**
     * Создание новой пиццы
     */
    @Test
    void createPizza() throws Exception {
        Pizza pizza = new Pizza(1L, "XL", "Tomatoes", null, "Margarita", 250.0);
        Cafe dbCafe = cafeRepository.findAll().get(0);
        Set<Pizza> pizzas = new HashSet<>();
        pizzas.add(pizza);
        dbCafe.setPizzas(pizzas);
        cafeRepository.save(dbCafe);
        pizza.setCafe(dbCafe);
        pizzaRepository.save(pizza);
        when(pizzaRepository.findById(eq(1L))).thenReturn(Optional.of(pizza));
        mockMvc.perform(post("/pizza")
                        .with(user("admin").password("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"size\":\"XL\",\"key_ingredients\":\"Tomatoes\", \"cafe\": " +
                                objectMapper.writeValueAsString(dbCafe) +
                                ",\"name\":\"Margarita\",\"price\":250.0}"))
                .andExpect(status().isCreated());
    }

    /**
     * Получение информации об указанной пицце
     */
    @Test
    void getPizzaById() throws Exception {
        Pizza pizza = new Pizza(1L, "XL", "Tomatoes", cafe, "Margarita", 250.0);

        when(pizzaRepository.findById(eq(1L))).thenReturn(Optional.of(pizza));

        mockMvc.perform(get("/pizza/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Margarita"))
                .andExpect(jsonPath("$.size").value("XL"))
                .andExpect(jsonPath("$.key_ingredients").value("Tomatoes"))
                .andExpect(jsonPath("$.price").value(250.0));
    }

    /**
     * Обновление пиццы по ее идентификатору
     */
    @Test
    void updatePizzaById() throws Exception {
        Pizza pizza = new Pizza(1L, "XL", "Tomatoes", cafe, "Margarita", 250.0);

        when(pizzaRepository.findById(eq(1L))).thenReturn(Optional.of(pizza));
        Pizza pizza1 = new Pizza();
        pizza1.setId(pizza.getId());
        pizza1.setSize(pizza.getSize());
        pizza1.setKey_ingredients(pizza.getKey_ingredients());
        pizza1.setCafe(pizza.getCafe());
        pizza1.setName(pizza.getName());
        pizza1.setPrice(500.0);

        mockMvc.perform(put("/pizza/{id}", 1L)
                        .with(user("admin").password("admin").roles("ADMIN"))
                        .content(objectMapper.writeValueAsString(pizza1))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Удаление пиццы по ее идентификатору
     */
    @Test
    void deletePizzaById() throws Exception {
        Pizza pizza = new Pizza(1L, "XL", "Tomatoes", cafe, "Margarita", 250.0);
        Set<Pizza> pizzas = new HashSet<>();
        pizzas.add(pizza);
        cafe.setPizzas(pizzas);

        mockMvc.perform(delete("/pizza/{id}", 1L)
                        .with(user("admin").password("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    /**
     * Поиск пиццы по названию
     */
    @Test
    void findPizzaByName() throws Exception {
        Pizza pizza = new Pizza(1L, "XL", "Tomatoes", cafe, "Margarita", 250.0);

        Set<Pizza> pizzas = new HashSet<>();
        pizzas.add(pizza);
        cafe.setPizzas(pizzas);

        mockMvc.perform(get("/pizzas")
                        .param("pizza_name", "Marg"))
                .andExpect(status().isOk());
    }
}

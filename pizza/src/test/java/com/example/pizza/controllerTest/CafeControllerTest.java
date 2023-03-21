package com.example.pizza.controllerTest;

import com.example.pizza.entity.Cafe;
import com.example.pizza.repository.CafeRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
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

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CafeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CafeRepository cafeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Получение списка всех кафе
     * @throws Exception
     */
    @Test
    void getAllCafes() throws Exception {
        mockMvc.perform(get("/cafes").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }

    /**
     * Создание нового кафе
     */
    @Test
    void createCafe() throws Exception {
        Cafe cafe = new Cafe(1L, "ParisCafe", "Paris",
                "Street2", "paris_cafe@gmail.com", "1234567", new HashSet<>());

        mockMvc.perform(post("/cafe")
                        .with(user("admin").password("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cafe)))
                .andExpect(status().isCreated());
    }

    /**
     * Получение полной информации об указанном кафе
     */
    @Test
    void getAllInfoAboutOneCafe() throws Exception {
        Cafe cafe = new Cafe(1L, "ParisCafe", "Paris",
                "Street2", "paris_cafe@gmail.com", "1234567", new HashSet<>());

        when(cafeRepository.findById(eq(1L))).thenReturn(Optional.of(cafe));

        mockMvc.perform(get("/cafe/full/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").isNotEmpty())
                .andExpect(jsonPath("$.city").value("Paris"))
                .andExpect(jsonPath("$.address").value("Street2"))
                .andExpect(jsonPath("$.email").value("paris_cafe@gmail.com"))
                .andExpect(jsonPath("$.phone").value("1234567"))
                .andExpect(jsonPath("$.pizzas").isEmpty());
    }

    /**
     * Обновление кафе по его идентификатору
     */
    @Test
    void updateCafeById() throws Exception {
        Cafe cafe = new Cafe(1L, "ParisCafe", "Paris",
                "Street2", "paris_cafe@gmail.com", "1234567", new HashSet<>());

        when(cafeRepository.findById(eq(1L))).thenReturn(Optional.of(cafe));
        Cafe newCafe = new Cafe(cafe);
        newCafe.setName("newCafe");
        newCafe.setId(1L);
        mockMvc.perform(put("/cafe/{id}", 1L)
                        .with(user("admin").password("admin").roles("ADMIN"))
                        .content(objectMapper.writeValueAsString(newCafe))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Удаление кафе по его идентификатору
     */
    @Test
    void deleteCafeById() throws Exception {
        Cafe cafe = new Cafe(1L, "ParisCafe", "Paris",
                "Street2", "paris_cafe@gmail.com", "1234567", new HashSet<>());
        mockMvc.perform(delete("/cafe/{id}", 1L)
                        .with(user("admin").password("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    /**
     * Поиск кафе по адресу
     */
    @Test
    void findCafeByAddress() throws Exception {
        Cafe cafe = new Cafe(1L, "ParisCafe", "Paris",
                "Street2", "paris_cafe@gmail.com", "1234567", new HashSet<>());
        mockMvc.perform(get("/cafes")
                        .param("cafe_address", "Stre"))
                .andExpect(status().isOk());
    }
}


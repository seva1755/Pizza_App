package com.example.pizza.controller;

import com.example.pizza.entity.Cafe;
import com.example.pizza.entity.Pizza;
import com.example.pizza.repository.CafeRepository;
import com.example.pizza.repository.PizzaRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;

@Validated
@RestController
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PizzaController {
    @Autowired
    PizzaRepository pizzaRepository;
    @Autowired
    private CafeRepository cafeRepository;

    /**
     * List all pizzas of specific cafe
     */
    @GetMapping(value = "/pizzas", params = "cafe_id")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public List<Pizza> findByName(@RequestParam(name = "cafe_id") @Min(1) Long cafeId){
        return pizzaRepository.findAllByCafeId(cafeId);
    }

    /**
     * Add new pizza to specific cafe
     */
    @PostMapping("/pizza")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Pizza> createPizza(@Valid @RequestBody Pizza pizza) {
        Optional<Cafe> cafeData = cafeRepository.findById(pizza.getCafe().getId());
        try {
            System.out.println(cafeData.get());
            cafeData.ifPresent(pizza::setCafe);
            return new ResponseEntity<>(pizzaRepository.save(pizza), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get specific pizza details
     */
    @GetMapping("/pizza/{id}")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<Pizza> getPizzaById(@PathVariable("id") @Min(1) long id) {
        Optional<Pizza> pizzaData = pizzaRepository.findById(id);

        return pizzaData.map(pizza -> new ResponseEntity<>(pizza, HttpStatus.OK))
                        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Update pizza details (by pizza id)
     */
    @PutMapping("/pizza/{id}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Pizza> updateById(@PathVariable @Min(1) Long id, @Valid @RequestBody Pizza pizza){
        Optional<Pizza> pizzaData = pizzaRepository.findById(id);
        if (pizzaData.isPresent()) {
            Pizza _pizza = pizzaData.get();
            _pizza.setId(id);
            _pizza.setName(pizza.getName());
            _pizza.setSize(pizza.getSize());
            _pizza.setKey_ingredients(pizza.getKey_ingredients());
            _pizza.setPrice(pizza.getPrice());
            return new ResponseEntity<>(pizzaRepository.save(_pizza), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    /**
     * Delete specific pizza
     */
    @DeleteMapping("/pizza/{id}")
    @Secured({"ROLE_ADMIN"})
    public  ResponseEntity<HttpStatus> deleteById(@PathVariable @Min(1) Long id){
        try {
            pizzaRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * List all pizzas from database
     */
    @GetMapping("/pizzas")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public List<Pizza> getAllPizzas(){
        return pizzaRepository.findAll();
    }

    /**
     * Basic search by pizza name (should return all pizzas whose name contains search term)
     */
    @GetMapping(value = "/pizzas", params = "pizza_name")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public List<Pizza> findByName(@RequestParam(name = "pizza_name") @NotBlank String pizza_name){
        return pizzaRepository.findAllByNameContainingIgnoreCase(pizza_name);
    }
}

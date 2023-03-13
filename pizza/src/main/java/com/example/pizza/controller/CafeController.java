package com.example.pizza.controller;

import com.example.pizza.entity.Cafe;
import com.example.pizza.repository.CafeRepository;
import com.example.pizza.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Validated
@RestController
public class CafeController {

    @Autowired
    CafeRepository cafeRepository;

    @Autowired
    PizzaRepository pizzaRepository;
    /**
     * List all cafes
     */
    @GetMapping("/cafes")
    public List<Cafe> getAllCafes(){
        return cafeRepository.findAll();
    }

    /**
     * Add a new cafe
     */
    @PostMapping("/cafe")
    public ResponseEntity<Cafe> createCafe(@Valid @RequestBody Cafe cafe) {
        try {
            return new ResponseEntity<>(cafeRepository.save(cafe), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get cafe by id with all pizza details listed
     */
    @GetMapping("/cafe/full/{id}")
    public ResponseEntity<Cafe> getCafeById(@PathVariable("id") long id) {
        Optional<Cafe> cafeData = cafeRepository.findById(id);
        if(cafeData.isPresent()){
            System.out.println(cafeData.get());
            Cafe cafe = cafeData.get();
            cafe.setPizzas(cafeData.get().getPizzas());
            System.out.println(cafeData.get().getPizzas());
            return new ResponseEntity<>(cafe, HttpStatus.OK);
        } else{
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    /**
     * Update cafe details (identified by id)
     */
    @PutMapping("/cafe/{id}")
    public ResponseEntity<Cafe> updateById(@PathVariable Long id, @Valid @RequestBody Cafe cafe){
        Optional<Cafe> cafeData = cafeRepository.findById(id);

        if (cafeData.isPresent()) {
            Cafe _cafe = cafeData.get();
            _cafe.setId(id);
            _cafe.setName(cafe.getName());
            _cafe.setCity(cafe.getCity());
            _cafe.setAddress(cafe.getAddress());
            _cafe.setEmail(cafe.getEmail());
            _cafe.setPhone(cafe.getPhone());
            return new ResponseEntity<>(cafeRepository.save(_cafe), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/cafe/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable Long id){
        try {
            cafeRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Basic search by cafe address (should return all cafes whose name contains search term)
     */
    @GetMapping(value = "/cafes", params = "cafe_address")
    public List<Cafe> findByAddress(@RequestParam(name = "cafe_address") String cafe_address){
        return cafeRepository.findAllByAddressContainingIgnoreCase(cafe_address);
    }
}

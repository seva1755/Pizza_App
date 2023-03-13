package com.example.pizza.repository;

import com.example.pizza.entity.Cafe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CafeRepository extends JpaRepository <Cafe, Long> {
    List<Cafe> findAllByAddressContainingIgnoreCase(String cafe_address);
}

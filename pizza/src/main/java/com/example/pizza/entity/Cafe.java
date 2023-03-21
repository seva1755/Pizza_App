package com.example.pizza.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Cafe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    Long id;

    @Column
    @NotBlank
    String name;

    @Column
    @NotBlank
    String city;

    @Column
    @NotBlank
    String address;

    @Column
    @Email
    String email;

    @Column
    String phone;

    @OneToMany(mappedBy = "cafe", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Pizza> pizzas = new HashSet<>();

    public Cafe(String name, String city, String address, String email, String phone) {
        this.name = name;
        this.city = city;
        this.address = address;
        this.email = email;
        this.phone = phone;
    }
    public Cafe(Cafe cafe) {
        this.name = cafe.getName();
        this.city = cafe.getCity();
        this.address = cafe.getAddress();
        this.email = cafe.getEmail();
        this.phone = cafe.getPhone();
    }

    public void setPizzas(Set<Pizza> pizzas) {
        this.pizzas = pizzas;
        pizzas.forEach(p -> p.setCafe(this));
    }

    @Override
    public String toString() {
        return "Cafe{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", pizzas=" + pizzas +
                '}';
    }
}
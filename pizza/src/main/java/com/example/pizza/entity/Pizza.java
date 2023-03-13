package com.example.pizza.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Entity(name = "pizza")
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Pizza {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name="pizza_size")
    //@NotBlank(message = "Size should be present")
    String size;

    @Column(name="key_ingredients")
    //@NotBlank(message = "Key ingredients should be present")
    String key_ingredients;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cafe_id"/*,nullable = false, foreignKey = @ForeignKey(name = "fk_pizzas_cafes_id")*/)
    //@JsonBackReference
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    Cafe cafe;

    @Column
    //@NotBlank(message = "Name should be present")
    String name;

    @Column
    //@NotBlank(message = "Price should be present")
    Double price;

    public Pizza(String name, String size, String key_ingredients, Cafe cafe, Double price) {
        this.name = name;
        this.size = size;
        this.key_ingredients = key_ingredients;
        this.cafe = cafe;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Pizza{" +
                "id=" + id +
                ", size=" + size +
                ", key_ingredients='" + key_ingredients + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}

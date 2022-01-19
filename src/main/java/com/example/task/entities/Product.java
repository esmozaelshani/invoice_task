package com.example.task.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@Entity
@NoArgsConstructor
public class Product {
    @Id
    private Integer id;
    private String name;
    private String details;
    private Double price;
    private Double discount;
    private Integer quantity;
    private Double vat;
    private Double total;

}

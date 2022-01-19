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
@NoArgsConstructor
@Entity
public class Invoice {
    @Id
    private Integer id;
    private String name;
    private Double total;
    private Double subtotal;
    private Double vat;
}

package com.example.task.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductModel {
    private Integer id;
    private String name;
    private String details;
    private Double price;
    private Double discount;
    private Double vat;
    private Integer quantity;
    private Double total= Double.valueOf(0);

}

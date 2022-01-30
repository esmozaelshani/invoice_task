package com.example.task.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CalculateModel {
    private Integer allowedProducts;
    private Double total = Double.valueOf(0);
    private Double subtotal = Double.valueOf(0);
    private Double vat = Double.valueOf(0);
}

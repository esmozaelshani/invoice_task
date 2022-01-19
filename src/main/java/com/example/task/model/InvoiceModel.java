package com.example.task.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InvoiceModel {
    private Integer id;
    private String name;
    private Double total = Double.valueOf(0);
    private Double subtotal = Double.valueOf(0);
    private Double vat = Double.valueOf(0);
    private List<ProductModel> products;
}

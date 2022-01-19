package com.example.task.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderModel {
    private Integer id;
    private String name;
    private List<ProductModel> products;
}

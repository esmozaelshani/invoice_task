package com.example.task.mapper;

import com.example.task.entities.Product;
import com.example.task.model.ProductModel;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public abstract class ProductMapper {

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "details", target = "details"),
            @Mapping(source = "price", target = "price"),
            @Mapping(source = "discount", target = "discount"),
            @Mapping(source = "quantity", target = "quantity"),
            @Mapping(source = "vat", target = "vat"),
            @Mapping(source = "total", target = "total")
    })
    public abstract ProductModel toModel(Product product);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "details", target = "details"),
            @Mapping(source = "price", target = "price"),
            @Mapping(source = "discount", target = "discount"),
            @Mapping(source = "quantity", target = "quantity"),
            @Mapping(source = "vat", target = "vat"),
            @Mapping(source = "total", target = "total")
    })
    public abstract Product toEntity(ProductModel productModel);
}

package com.example.task.mapper;

import com.example.task.entities.Order;
import com.example.task.model.OrderModel;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public abstract class OrderMapper {

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "products", target = "products")
    })
    public abstract OrderModel toModel(Order order);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "products", target = "products")
    })
    public abstract Order toEntity(OrderModel orderModel);
}

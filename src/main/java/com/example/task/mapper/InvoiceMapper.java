package com.example.task.mapper;

import com.example.task.entities.Invoice;
import com.example.task.model.InvoiceModel;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public abstract class InvoiceMapper {

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "total", target = "total"),
            @Mapping(source = "subtotal", target = "subtotal"),
            @Mapping(source = "vat", target = "vat")
    })
    public abstract InvoiceModel toModel(Invoice invoice);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "total", target = "total"),
            @Mapping(source = "subtotal", target = "subtotal"),
            @Mapping(source = "vat", target = "vat")    })
    public abstract Invoice toEntity(InvoiceModel invoiceModel);
}

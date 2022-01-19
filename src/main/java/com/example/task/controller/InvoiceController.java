package com.example.task.controller;

import com.example.task.model.InvoiceModel;
import com.example.task.model.OrderModel;
import com.example.task.service.InvoiceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {

    private InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService=invoiceService;
    }

    @PutMapping("/generate")
    public List<InvoiceModel> generateInvoices(@RequestBody OrderModel orderModel) {
        return invoiceService.generateInvoice(orderModel);
    }
}

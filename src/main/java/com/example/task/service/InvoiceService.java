package com.example.task.service;

import com.example.task.entities.Product;
import com.example.task.mapper.InvoiceMapper;
import com.example.task.mapper.OrderMapper;
import com.example.task.mapper.ProductMapper;
import com.example.task.model.InvoiceModel;
import com.example.task.model.OrderModel;
import com.example.task.model.ProductModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("Invoice")
public class InvoiceService {

    private final OrderMapper orderMapper;
    private final InvoiceMapper invoiceMapper;
    private final ProductMapper productMapper;
    List<ProductModel> products = new ArrayList<>();
    List<InvoiceModel> invoiceList = new ArrayList<>();


    public InvoiceService(OrderMapper orderMapper, InvoiceMapper invoiceMapper, ProductMapper productMapper){
        this.orderMapper = orderMapper;
        this.invoiceMapper = invoiceMapper;
        this.productMapper = productMapper;

    }

    public List<InvoiceModel> generateInvoice(OrderModel orderModel) {
        invoiceList.clear();
        products = orderModel.getProducts();
        calculatePrice();
        return invoiceList;
    }

    public void calculatePrice() {
        List<ProductModel> tempProducts = new ArrayList<>();
        List<ProductModel> removePrd = new ArrayList<>();
        InvoiceModel invoiceModel = new InvoiceModel();
        Double tempAmount = Double.valueOf(0);
        Double total = Double.valueOf(0);
        for (ProductModel product : this.products) {
            Product prd = productMapper.toEntity(product);

            total = calculatedPrice(prd).get("total");
            tempAmount = tempAmount + calculatedPrice(prd).get("total");
            if (product.getPrice() > 500 && tempProducts.isEmpty()) {
                ProductModel tempProduct = new ProductModel();
                tempProduct.setName(product.getName());
                tempProduct.setQuantity(1);
                tempProduct.setPrice(product.getPrice());
                tempProduct.setDiscount(product.getDiscount());
                tempProduct.setVat(product.getVat());
                Product toEntityProduct = productMapper.toEntity(tempProduct);
                tempProduct.setTotal(product.getTotal());
                invoiceModel.setTotal(calculatedPrice(toEntityProduct).get("total"));
                invoiceModel.setVat(calculatedPrice(toEntityProduct).get("vat"));
                invoiceModel.setSubtotal(calculatedPrice(toEntityProduct).get("subTotal"));
                tempProducts.add(tempProduct);
                if (product.getQuantity() > 1) {
                    product.setQuantity(product.getQuantity() - 1);
                } else {
                    removePrd.add(product);
                }
            } else if (tempAmount < 500) {
                if (product.getQuantity() <= 50) {
                    tempProducts.add(product);
                    invoiceModel.setTotal(invoiceModel.getTotal() + total);
                    invoiceModel.setVat(invoiceModel.getVat() + calculatedPrice(productMapper.toEntity(product)).get("vat"));
                    invoiceModel.setSubtotal(invoiceModel.getSubtotal() + calculatedPrice(productMapper.toEntity(product)).get("subTotal"));
                    removePrd.add(product);
                } else {
                    ProductModel tempProduct = new ProductModel();
                    tempProduct.setQuantity(50);
                    tempProduct.setName(product.getName());
                    tempProduct.setPrice(product.getPrice());
                    tempProduct.setDiscount(product.getDiscount());
                    tempProduct.setVat(product.getVat());
                    tempProduct.setTotal(product.getTotal());
                    invoiceModel.setTotal(invoiceModel.getTotal() + total);
                    invoiceModel.setVat(invoiceModel.getVat() + calculatedPrice(productMapper.toEntity(product)).get("vat"));
                    invoiceModel.setSubtotal(invoiceModel.getSubtotal() + calculatedPrice(productMapper.toEntity(product)).get("subTotal"));
                    product.setQuantity(product.getQuantity() - 50);
                    tempProducts.add(tempProduct);
                }

            }
        }
        products.removeAll(removePrd);
        invoiceModel.setProducts(tempProducts);
        invoiceList.add(invoiceModel);

        if (!products.isEmpty()) {
            calculatePrice();
        }
    }


    public Map<String,Double> calculatedPrice(Product product) {
        if(product.getQuantity() >50){
            product.setQuantity(50);
        }
        Double subTotal = product.getDiscount() != 0 ? (double) product.getQuantity() * (product.getPrice() - product.getDiscount()) : (double) product.getQuantity() * product.getPrice();
        Double vat = (subTotal * product.getVat()) / 100;
        Double total = subTotal + vat;
        return Map.of("subTotal", subTotal, "vat",vat, "total", total);
    }
}

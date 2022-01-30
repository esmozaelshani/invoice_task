package com.example.task.service;

import com.example.task.entities.Product;
import com.example.task.mapper.InvoiceMapper;
import com.example.task.mapper.OrderMapper;
import com.example.task.mapper.ProductMapper;
import com.example.task.model.CalculateModel;
import com.example.task.model.InvoiceModel;
import com.example.task.model.OrderModel;
import com.example.task.model.ProductModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("Invoice")
public class InvoiceService {

    private final OrderMapper orderMapper;
    private final InvoiceMapper invoiceMapper;
    private final ProductMapper productMapper;
    List<ProductModel> products = new ArrayList<>();
    List<InvoiceModel> invoiceList = new ArrayList<>();
    List<ProductModel> productList = new ArrayList<>();


    public InvoiceService(OrderMapper orderMapper, InvoiceMapper invoiceMapper, ProductMapper productMapper) {
        this.orderMapper = orderMapper;
        this.invoiceMapper = invoiceMapper;
        this.productMapper = productMapper;

    }

    public List<InvoiceModel> generateInvoice(OrderModel orderModel) {
        invoiceList.clear();
        productList.clear();
        products = orderModel.getProducts();
        generateProductList();
        return invoiceList;
    }

    public void generateProductList() {
        List<ProductModel> removePrd = new ArrayList<>();
        Double tempAmount = Double.valueOf(0);
        Double total = Double.valueOf(0);
        for (ProductModel product : this.products) {
            CalculateModel calc = calculatedPrice(productMapper.toEntity(product));
            total = calc.getTotal();
            ProductModel tempProduct = new ProductModel();
            if (total >= 500 && product.getQuantity() == 1) {
                productList.add(product);
                removePrd.add(product);
            } else if (total >= 500 && product.getQuantity() > 1) {
                tempProduct.setName(product.getName());
                tempProduct.setPrice(product.getPrice());
                tempProduct.setDiscount(product.getDiscount());
                tempProduct.setVat(product.getVat());
                tempProduct.setQuantity(calc.getAllowedProducts());
                tempProduct.setTotal(calc.getTotal());
                productList.add(tempProduct);
                if (product.getQuantity() > calc.getAllowedProducts()) {
                    product.setQuantity(product.getQuantity() - calc.getAllowedProducts());
                } else {
                    removePrd.add(product);
                }
            } else {
                tempAmount = tempAmount + total;
                if (tempAmount >= 500 && productList.isEmpty()) {
                    tempProduct.setName(product.getName());
                    tempProduct.setPrice(product.getPrice());
                    tempProduct.setDiscount(product.getDiscount());
                    tempProduct.setVat(product.getVat());
                    tempProduct.setQuantity(calc.getAllowedProducts());
                    tempProduct.setTotal(product.getTotal());

                    if (product.getQuantity() > calc.getAllowedProducts()) {
                        product.setQuantity(product.getQuantity() - calc.getAllowedProducts());
                    } else {
                        removePrd.add(product);
                    }
                    productList.add(tempProduct);
                } else if (tempAmount < 500) {
                    if (calc.getAllowedProducts() <= 50) {
                        tempProduct.setName(product.getName());
                        tempProduct.setPrice(product.getPrice());
                        tempProduct.setDiscount(product.getDiscount());
                        tempProduct.setVat(product.getVat());
                        tempProduct.setQuantity(calc.getAllowedProducts());
                        tempProduct.setTotal(calc.getTotal());
                        if (product.getQuantity() > calc.getAllowedProducts()) {
                            product.setQuantity(product.getQuantity() - calc.getAllowedProducts());
                        } else {
                            removePrd.add(product);
                        }
                        productList.add(tempProduct);
                    } else {
                        tempProduct.setQuantity(50);
                        tempProduct.setName(product.getName());
                        tempProduct.setPrice(product.getPrice());
                        tempProduct.setDiscount(product.getDiscount());
                        tempProduct.setVat(product.getVat());
                        tempProduct.setTotal(product.getTotal());
                        product.setQuantity(product.getQuantity() - 50);
                        removePrd.add(product);
                        productList.add(tempProduct);
                    }

                } else {
                    tempAmount = tempAmount - total;
                }
            }
        }
        products.removeAll(removePrd);
        if (!products.isEmpty()) {
            generateProductList();
        }
        calculateInvoice();
    }

    public void calculateInvoice() {
        List<ProductModel> tempProducts = new ArrayList<>();
        List<ProductModel> removePrd = new ArrayList<>();
        InvoiceModel invoiceModel = new InvoiceModel();
        Double tempAmount = Double.valueOf(0);
        Double total = Double.valueOf(0);
        for (ProductModel product : this.productList) {
            CalculateModel calc = calculatedPrice(productMapper.toEntity(product));
            total = calc.getTotal();
            tempAmount = tempAmount + total;
            if (total >= 500 && tempProducts.isEmpty() && tempProducts.stream().filter(x -> x.getName().equals(product.getName())).collect(Collectors.toList()).isEmpty()) {
                invoiceModel.setTotal(calc.getTotal());
                invoiceModel.setVat(calc.getVat());
                invoiceModel.setSubtotal(calc.getSubtotal());
                tempProducts.add(product);
                removePrd.add(product);
            } else if (tempAmount < 500 && tempProducts.stream().filter(x -> x.getName().equals(product.getName())).collect(Collectors.toList()).isEmpty()) {
                if (product.getQuantity() <= 50) {
                    tempProducts.add(product);
                    invoiceModel.setTotal(invoiceModel.getTotal() + total);
                    invoiceModel.setVat(invoiceModel.getVat() + calc.getVat());
                    invoiceModel.setSubtotal(invoiceModel.getSubtotal() + calc.getSubtotal());
                    if (product.getQuantity() > calc.getAllowedProducts()) {
                        product.setQuantity(product.getQuantity() - calc.getAllowedProducts());
                    } else {
                        removePrd.add(product);
                    }
                } else {
                    ProductModel tempProduct = new ProductModel();
                    tempProduct.setQuantity(50);
                    tempProduct.setName(product.getName());
                    tempProduct.setPrice(product.getPrice());
                    tempProduct.setDiscount(product.getDiscount());
                    tempProduct.setVat(product.getVat());
                    tempProduct.setTotal(product.getTotal());
                    invoiceModel.setTotal(invoiceModel.getTotal() + total);
                    invoiceModel.setVat(invoiceModel.getVat() + calc.getVat());
                    invoiceModel.setSubtotal(invoiceModel.getSubtotal() + calc.getSubtotal());
                    product.setQuantity(product.getQuantity() - 50);
                    tempProducts.add(product);
                    removePrd.add(product);
                    ;
                }

            }
        }
        productList.removeAll(removePrd);
        invoiceModel.setProducts(tempProducts);
        invoiceList.add(invoiceModel);

        if (!products.isEmpty()) {
            calculateInvoice();
        }
    }


    public CalculateModel calculatedPrice(Product product) {
        CalculateModel calculateModel = new CalculateModel();
        if (product.getQuantity() > 50) {
            product.setQuantity(50);
        }
        Product temp = new Product();
        temp.setQuantity(product.getQuantity());
        temp.setPrice(product.getPrice());
        temp.setDiscount(product.getDiscount());
        temp.setVat(product.getVat());
        Integer allowedQuantity = (int) Math.floor(500 / product.getPrice());
        if (allowedQuantity == 0) {
            temp.setQuantity(1);
        } else {
            temp.setQuantity(allowedQuantity);
        }
        if (product.getQuantity() < allowedQuantity) {
            allowedQuantity = product.getQuantity();
        }
        if (product.getQuantity() < temp.getQuantity()) {
            temp.setQuantity(product.getQuantity());
        }
        int quantity = 0;
        while (computeTotal(temp) > 500) {
            quantity++;
            temp.setQuantity(allowedQuantity - quantity);
            allowedQuantity = allowedQuantity - quantity;

        }

        Double subTotal = product.getDiscount() != 0 ? (double) allowedQuantity * (product.getPrice() - product.getDiscount()) : (double) allowedQuantity * product.getPrice();
        Double vat = (subTotal * product.getVat()) / 100;
        Double total = subTotal + vat;
        calculateModel.setAllowedProducts(allowedQuantity);
        calculateModel.setVat(vat);
        calculateModel.setTotal(total);
        calculateModel.setSubtotal(subTotal);
        return calculateModel;
    }

    public double computeTotal(Product product) {
        double subTotal = product.getDiscount() != 0 ? (double) product.getQuantity() * (product.getPrice() - product.getDiscount()) : (double) product.getQuantity() * product.getPrice();
        double vat = (subTotal * product.getVat()) / 100;
        return subTotal + vat;
    }
}

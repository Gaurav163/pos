package com.increff.invoice.model;

import lombok.Data;

@Data
public class OrderItemForm {
    private Long id;
    private String name;
    private String barcode;
    private Double mrp;
    private Double sellingPrice;
    private Long quantity;
    private String brand;
    private String category;
}

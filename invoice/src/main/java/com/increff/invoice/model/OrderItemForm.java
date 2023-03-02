package com.increff.invoice.model;

import lombok.Data;

@Data
public class OrderItemForm {
    private String name;
    private String barcode;
    private Double sellingPrice;
    private Long quantity;
    private String brand;
    private String category;
}

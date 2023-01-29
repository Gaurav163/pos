package com.increff.pos.model;

import lombok.Data;

@Data
public class OrderItemForm {
    private String barcode;
    private Long quantity;
    private Double sellingPrice;
}

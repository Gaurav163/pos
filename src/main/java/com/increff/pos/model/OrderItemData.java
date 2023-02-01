package com.increff.pos.model;

import lombok.Data;

@Data
public class OrderItemData {
    private Long id;
    private Long orderId;
    private String name;
    private String barcode;
    private Double mrp;
    private Double sellingPrice;
    private Long quantity;
}

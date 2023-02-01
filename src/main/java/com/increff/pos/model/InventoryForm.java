package com.increff.pos.model;

import lombok.Data;

@Data
public class InventoryForm {
    private String barcode;
    private Long quantity;
}

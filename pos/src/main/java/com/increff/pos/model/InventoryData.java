package com.increff.pos.model;

import lombok.Data;

@Data
public class InventoryData extends InventoryForm {
    private String name;
    private String brand;
    private String category;
}

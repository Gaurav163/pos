package com.increff.pos.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductForm {
    private String name;
    private String barcode;
    private Double mrp;
    private Long brandId;
}

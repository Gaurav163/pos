package com.increff.pos.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductData extends ProductForm {
    private Long id;
    private String brandName;
    private String brandCategory;
    private Long quantity;
}

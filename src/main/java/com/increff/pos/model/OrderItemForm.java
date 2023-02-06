package com.increff.pos.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class OrderItemForm {
    @NotBlank
    private String barcode;
    @NotNull
    private Long quantity;
    @NotNull
    private Double sellingPrice;
}

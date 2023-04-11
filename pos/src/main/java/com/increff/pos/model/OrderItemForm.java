package com.increff.pos.model;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class OrderItemForm {
    @NotBlank
    private String barcode;
    @NotNull
    @Min(value = 1)
    private Long quantity;
    @NotNull
    @Min(value = 0)
    private Double sellingPrice;
}

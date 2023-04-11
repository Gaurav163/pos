package com.increff.pos.model;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class InventoryForm {
    @NotBlank
    private String barcode;
    @NotNull
    @Min(value = 0)
    private Long quantity;
}

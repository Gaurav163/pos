package com.increff.pos.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class ProductForm {
    @NotBlank
    private String name;
    @NotBlank
    private String barcode;
    @NotNull
    @Min(value = 0)
    private Double mrp;
    @NotNull
    @Min(value = 1)
    private Long brandId;
}

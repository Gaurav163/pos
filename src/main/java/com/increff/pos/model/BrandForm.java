package com.increff.pos.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class BrandForm {
    @NotBlank(message = "Brand name should not be empty")
    private String name;
    @NotBlank(message = "Category should not be empty")
    private String category;
}

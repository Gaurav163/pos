package com.increff.pos.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class BrandForm {
    @NotBlank()
    private String name;
    @NotBlank()
    private String category;
}

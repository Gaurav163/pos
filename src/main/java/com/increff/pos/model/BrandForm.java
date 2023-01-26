package com.increff.pos.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class BrandForm {
    @NotNull
    private String name;
    private String category;
}

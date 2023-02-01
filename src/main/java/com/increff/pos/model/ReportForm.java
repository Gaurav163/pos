package com.increff.pos.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ReportForm {
    @NotBlank
    private String startDate;
    @NotBlank
    private String endDate;
    private String brand;
    private String category;
}

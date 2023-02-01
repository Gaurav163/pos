package com.increff.pos.model;

import lombok.Data;

@Data
public class ReportData {
    private String brand;
    private String category;
    private Long quantity;
    private Double revenue;
}

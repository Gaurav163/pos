package com.increff.pdf.model;

import lombok.Data;

import java.util.List;

@Data
public class OrderData {
    private Long id;
    private String date;
    private String time;
    private Boolean invoiced;
    private List<OrderItemData> items;
}

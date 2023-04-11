package com.increff.pos.model;

import lombok.Data;

import java.util.List;

@Data
public class OrderData {
    private Long id;
    private String datetime;
    private Boolean invoiced;
    private List<OrderItemData> items;
}

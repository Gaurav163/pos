package com.increff.invoice.model;

import lombok.Data;

import java.util.List;

@Data
public class OrderForm {
    private Long id;
    private List<OrderItemForm> items;
}

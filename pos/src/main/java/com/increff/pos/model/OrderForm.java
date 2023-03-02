package com.increff.pos.model;

import lombok.Data;

import java.util.List;

@Data
public class OrderForm {
    private List<OrderItemForm> items;
}

package com.increff.pos.model;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class OrderForm {
    @Valid
    @Size(min = 1)
    private List<OrderItemForm> items;
}

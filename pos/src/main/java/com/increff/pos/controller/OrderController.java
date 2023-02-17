package com.increff.pos.controller;

import com.increff.pos.dto.OrderDto;
import com.increff.pos.model.ApiException;
import com.increff.pos.model.OrderData;
import com.increff.pos.model.OrderItemForm;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderDto orderDto;

    @ApiOperation(value = "Create order with list of order-item list")
    @RequestMapping(path = "/", method = RequestMethod.POST)
    public OrderData create(@RequestBody List<OrderItemForm> items) throws ApiException {
        return orderDto.create(items);
    }

    @ApiOperation(value = "Get all order")
    @RequestMapping(path = "/", method = RequestMethod.GET)
    public List<OrderData> getAll() throws ApiException {
        return orderDto.getAll();
    }

    @ApiOperation(value = "Get order details by order ID")
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public OrderData getById(@PathVariable("id") Long id) throws ApiException {
        return orderDto.getById(id);
    }

    @ApiOperation(value = "Get order invoice by order ID in base64 format")
    @RequestMapping(path = "/invoice/{id}", method = RequestMethod.GET)
    public String getInvoiceAsBase64(@PathVariable("id") Long id) throws ApiException {
        return orderDto.getInvoiceAsBase64(id);
    }

    @ApiOperation(value = "Generate order invoice by order ID as pdf")
    @RequestMapping(path = "/invoice/{id}", method = RequestMethod.POST)
    public OrderData generateInvoice(@PathVariable("id") Long id) throws ApiException {
        return orderDto.generateInvoice(id);
    }

}

package com.increff.pos.controller;

import com.increff.pos.dto.OrderDto;
import com.increff.pos.model.ApiException;
import com.increff.pos.model.OrderData;
import com.increff.pos.model.OrderForm;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderDto orderDto;

    @ApiOperation(value = "Create order with given list of order-item")
    @RequestMapping(path = "/", method = RequestMethod.POST)
    public OrderData create(@RequestBody OrderForm orderForm) throws ApiException {
        return orderDto.create(orderForm);
    }

    @ApiOperation(value = "Get all orders")
    @RequestMapping(path = "/", method = RequestMethod.GET)
    public List<OrderData> getAll(@RequestParam(required = false, name = "size") Long size, @RequestParam(required = false, name = "lastId") Long lastId) throws ApiException {
        return orderDto.getBySizeAndPage(size, lastId);
    }

    @ApiOperation(value = "Get order details")
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public OrderData getById(@PathVariable("id") Long id) throws ApiException {
        return orderDto.getById(id);
    }

    @ApiOperation(value = "Get order invoice")
    @RequestMapping(path = "/{id}/invoice", method = RequestMethod.GET)
    public String getInvoiceAsBase64(@PathVariable("id") Long id) throws ApiException {
        return orderDto.getInvoice(id);
    }

}

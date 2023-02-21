package com.increff.invoice.controller;

import com.increff.invoice.model.ApiException;
import com.increff.invoice.model.OrderForm;
import com.increff.invoice.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InvoiceController {
    @Autowired
    private InvoiceService invoiceService;

    @RequestMapping(value = "/invoice/", method = RequestMethod.POST)
    public String createInvoice(@RequestBody OrderForm order) throws ApiException {
        return invoiceService.generateInvoice(order);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String ping() {
        return " Hello, Service is up";
    }
}

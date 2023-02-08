package com.increff.pdf.controller;

import com.increff.pdf.model.ApiException;
import com.increff.pdf.model.OrderData;
import com.increff.pdf.service.PdfService;
import org.springframework.web.bind.annotation.*;

@RestController
public class PdfController {

    @RequestMapping(value = "/invoice/",method = RequestMethod.POST)
    public String createInvoice(@RequestBody OrderData order) throws ApiException {
        return PdfService.convertToPDF(order);
    }

    @RequestMapping(value="/",method = RequestMethod.GET)
    public String hello(){
        return " Hello, Service is up";
    }
}

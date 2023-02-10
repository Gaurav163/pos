package com.increff.pos.controller;

import com.increff.pos.dto.ProductDto;
import com.increff.pos.model.ApiException;
import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api
@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductDto productDto;

    @Autowired
    public ProductController(ProductDto productDto) {
        this.productDto = productDto;
    }

    @ApiOperation(value = "Create Product")
    @RequestMapping(path = "/", method = RequestMethod.POST)
    public ProductData create(@RequestBody ProductForm brandForm) throws ApiException {
        return productDto.create(brandForm);
    }

    @ApiOperation(value = "Create multiple products using tsv file")
    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    public void uploadProducts(@RequestPart("file") MultipartFile file) throws ApiException {
        productDto.upload(file);
    }

    @ApiOperation(value = "Get product by Id")
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public ProductData getById(@PathVariable("id") Long id) throws ApiException {
        return productDto.getById(id);
    }


    @ApiOperation(value = "Get all products")
    @RequestMapping(path = "/", method = RequestMethod.GET)
    public List<ProductData> getAll() throws ApiException {
        return productDto.getAll();
    }

    @ApiOperation(value = "Update product with provided ID")
    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public ProductData update(@RequestBody ProductForm brandForm, @PathVariable("id") Long id) throws ApiException {
        return productDto.update(id, brandForm);
    }
}

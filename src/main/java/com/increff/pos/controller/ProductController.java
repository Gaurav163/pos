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
    @PostMapping(path = "/", value = "Create New Product !")
    public ProductData create(@RequestBody ProductForm brandForm) throws ApiException {
        return productDto.create(brandForm);
    }

    @ApiOperation(value = "Create multiple Products using tsv file.")
    @PostMapping("/upload")
    public List<String> uploadProducts(@RequestPart("file") MultipartFile file) throws ApiException {
        return productDto.upload(file);
    }

    @ApiOperation(value = "Get Product By Id")
    @GetMapping("/{id}")
    public ProductData getById(@PathVariable("id") Long id) throws ApiException {
        return productDto.getById(id);
    }

    @ApiOperation(value = "Get Product By Barcode")
    @GetMapping("/barcode/{barcode}")
    public ProductData getByBarcode(@PathVariable("barcode") String barcode) throws ApiException {
        return productDto.getByBarcode(barcode);
    }

    @ApiOperation(value = "Get All Products")
    @GetMapping("/")
    public List<ProductData> getAll() throws ApiException {
        return productDto.getAll();
    }

    @ApiOperation(value = "Update Product with Given Id and Provided Data.")
    @PutMapping("/{id}")
    public ProductData update(@RequestBody ProductForm brandForm, @PathVariable("id") Long id) throws ApiException {
        return productDto.update(id, brandForm);
    }
}

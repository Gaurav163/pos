package com.increff.pos.controller;

import com.increff.pos.dto.BrandDto;
import com.increff.pos.model.ApiException;
import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Api
@RestController
@RequestMapping("/api/brands")
public class BrandController {
    private final BrandDto brandDto;

    @Autowired
    public BrandController(BrandDto brandDto) {
        this.brandDto = brandDto;
    }

    @ApiOperation(value = "Create Brand")
    @PostMapping(path = "/", value = "Create New Brand !")
    public BrandData create(@RequestBody BrandForm brandForm) throws ApiException {
        return brandDto.create(brandForm);
    }

    @ApiOperation(value = "Create multiple Brands using tsv file.")
    @PostMapping("/upload")
    public List<String> uploadBrands(@RequestPart("file") MultipartFile file) throws IOException, ApiException {
        return brandDto.upload(file);
    }

    @ApiOperation(value = "Get Brand By Id")
    @GetMapping("/{id}")
    public BrandData getById(@PathVariable("id") Long id) throws ApiException {
        return brandDto.getById(id);
    }

    @ApiOperation(value = "Get All Brands")
    @GetMapping("/")
    public List<BrandData> getAll() throws ApiException {
        return brandDto.getAll();
    }

    @ApiOperation(value = "Get All Brands With Name Provided")
    @GetMapping("/name/{name}")
    public List<BrandData> getByName(@PathVariable("name") String name) throws ApiException {
        return brandDto.getByName(name);
    }

    @ApiOperation(value = "Get All Brands With Category Provided")
    @GetMapping("/category/{category}")
    public List<BrandData> getByCategory(@PathVariable("category") String category) throws ApiException {
        return brandDto.getByCategory(category);
    }

    @ApiOperation(value = "Update Brand with Given Id and Provided Data.")
    @PutMapping("/{id}")
    public BrandData update(@RequestBody BrandForm brandForm, @PathVariable("id") Long id) throws ApiException {
        return brandDto.update(id, brandForm);
    }

}

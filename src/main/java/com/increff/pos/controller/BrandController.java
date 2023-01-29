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
    @Autowired
    private BrandDto brandDto;

    @ApiOperation(value = "Create brand with provided name and category")
    @RequestMapping(path = "/", method = RequestMethod.POST)
    public BrandData create(@RequestBody BrandForm brandForm) throws ApiException {
        return brandDto.create(brandForm);
    }

    @ApiOperation(value = "Create multiple brands using tsv file")
    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    public List<String> uploadBrands(@RequestPart("file") MultipartFile file) throws IOException, ApiException {
        return brandDto.upload(file);
    }

    @ApiOperation(value = "Get brand by ID")
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public BrandData getById(@PathVariable("id") Long id) throws ApiException {
        return brandDto.getById(id);
    }

    @ApiOperation(value = "Get all brands")
    @RequestMapping(path = "/", method = RequestMethod.GET)
    public List<BrandData> getAll() throws ApiException {
        return brandDto.getAll();
    }

    @ApiOperation(value = "Get all brands with name provided")
    @RequestMapping(path = "/name/{name}", method = RequestMethod.GET)
    public List<BrandData> getByName(@PathVariable("name") String name) throws ApiException {
        return brandDto.getByName(name);
    }

    @ApiOperation(value = "Get all brands with category provided")
    @RequestMapping(path = "/category/{category}", method = RequestMethod.GET)
    public List<BrandData> getByCategory(@PathVariable("category") String category) throws ApiException {
        return brandDto.getByCategory(category);
    }

    @ApiOperation(value = "Update brand with given ID and provided data")
    @PutMapping("/{id}")
    public BrandData update(@RequestBody BrandForm brandForm, @PathVariable("id") Long id) throws ApiException {
        return brandDto.update(id, brandForm);
    }

}

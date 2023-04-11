package com.increff.pos.controller;

import com.increff.pos.model.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.BrandService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/brands")
public class BrandController {
    private final BrandService brandService;
    private final ModelMapper modelMapper;

    @Autowired
    public BrandController(BrandService brandService) {
        this.brandService = brandService;
        this.modelMapper = new ModelMapper();
    }

    @PostMapping("/")
    public ResponseEntity<BrandForm> saveBrand(@RequestBody BrandForm brandForm) {
        if (brandForm.getName() == null || brandForm.getCategory() == null) {
            throw new IllegalStateException("name And category are required fields!");
        }
        BrandPojo brand = modelMapper.map(brandForm, BrandPojo.class);
        brand = brandService.save(brand);
        return ResponseEntity.ok().body(modelMapper.map(brand, BrandForm.class));

    }

    @GetMapping("/brand/{id}")
    public BrandForm getById(@PathVariable("id") Long id) {
        BrandPojo brand = brandService.getById(id);
        return modelMapper.map(brand, BrandForm.class);
    }

    @GetMapping("/all")
    public List<BrandForm> getAll() {
        List<BrandPojo> brands = brandService.getAll();
        return Arrays.asList(modelMapper.map(brands, BrandForm[].class));
    }

    @GetMapping("/name/{name}")
    public List<BrandForm> getByName(@PathVariable("name") String name) {
        List<BrandPojo> brands = brandService.getByName(name);
        return Arrays.asList(modelMapper.map(brands, BrandForm[].class));
    }

    @GetMapping("/category/{category}")
    public List<BrandForm> getByCategory(@PathVariable("category") String category) {
        List<BrandPojo> brands = brandService.getByCategory(category);
        return Arrays.asList(modelMapper.map(brands, BrandForm[].class));
    }

    @PutMapping("/{id}")
    public BrandForm update(@RequestBody BrandForm brandForm, @PathVariable("id") Long id) {
        if (brandForm.getName() == null || brandForm.getCategory() == null) {
            throw new IllegalStateException("name And category are required fields!");
        }
        BrandPojo brand = modelMapper.map(brandForm, BrandPojo.class);
        return modelMapper.map(brandService.update(id, brand), BrandForm.class);
    }


}

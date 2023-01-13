package com.increff.pos.controller;

import com.increff.pos.dto.BrandDto;
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
    public ResponseEntity<BrandDto> saveBrand(@RequestBody BrandDto brandDto) {
        if (brandDto.getName() == null || brandDto.getCategory() == null) {
            throw new IllegalStateException("name And category are required fields!");
        }
        BrandPojo brand = modelMapper.map(brandDto, BrandPojo.class);
        brand = brandService.save(brand);
        return ResponseEntity.ok().body(modelMapper.map(brand, BrandDto.class));

    }

    @GetMapping("/brand/{id}")
    public BrandDto getById(@PathVariable("id") Long id) {
        BrandPojo brand = brandService.getById(id);
        return modelMapper.map(brand, BrandDto.class);
    }

    @GetMapping("/all")
    public List<BrandDto> getAll() {
        List<BrandPojo> brands = brandService.getAll();
        return Arrays.asList(modelMapper.map(brands, BrandDto[].class));
    }

    @GetMapping("/name/{name}")
    public List<BrandDto> getByName(@PathVariable("name") String name) {
        List<BrandPojo> brands = brandService.getByName(name);
        return Arrays.asList(modelMapper.map(brands, BrandDto[].class));
    }

    @GetMapping("/category/{category}")
    public List<BrandDto> getByCategory(@PathVariable("category") String category) {
        List<BrandPojo> brands = brandService.getByCategory(category);
        return Arrays.asList(modelMapper.map(brands, BrandDto[].class));
    }

    @PutMapping("/{id}")
    public BrandDto update(@RequestBody BrandDto brandDto, @PathVariable("id") Long id) {
        if (brandDto.getName() == null || brandDto.getCategory() == null) {
            throw new IllegalStateException("name And category are required fields!");
        }
        BrandPojo brand = modelMapper.map(brandDto, BrandPojo.class);
        return modelMapper.map(brandService.update(id, brand), BrandDto.class);
    }


}

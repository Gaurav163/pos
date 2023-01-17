package com.increff.pos.controller;

import com.increff.pos.dto.BrandDto;
import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/brands")
public class BrandController {
    private final BrandDto brandDto;

    @Autowired
    public BrandController(BrandDto brandDto) {
        this.brandDto = brandDto;
    }

    @PostMapping("/")
    public BrandData save(@RequestBody BrandForm brandForm) {
        return brandDto.save(brandForm);
    }

    @PostMapping("/upload")
    public List<String> uploadBrands(@RequestParam("file") MultipartFile file) throws IOException {
        return brandDto.upload(file);
    }

    @GetMapping("/{id}")
    public BrandData getById(@PathVariable("id") Long id) {
        return brandDto.getById(id);
    }

    @GetMapping("/")
    public List<BrandData> getAll() {
        return brandDto.getAll();
    }

    @GetMapping("/name/{name}")
    public List<BrandData> getByName(@PathVariable("name") String name) {
        return brandDto.getByName(name);
    }

    @GetMapping("/category/{category}")
    public List<BrandData> getByCategory(@PathVariable("category") String category) {
        return brandDto.getByCategory(category);
    }

    @PutMapping("/{id}")
    public BrandData update(@RequestBody BrandForm brandForm, @PathVariable("id") Long id) {
        return brandDto.update(id, brandForm);
    }


}

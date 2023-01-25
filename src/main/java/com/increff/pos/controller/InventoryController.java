package com.increff.pos.controller;


import com.increff.pos.dto.InventoryDto;
import com.increff.pos.model.ApiException;
import com.increff.pos.model.InventoryForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/inventories")
public class InventoryController {
    private final InventoryDto inventoryDto;

    @Autowired
    public InventoryController(InventoryDto inventoryDto) {
        this.inventoryDto = inventoryDto;
    }

    @PostMapping("/upload")
    public void upload(@RequestPart("file") MultipartFile file) throws ApiException {
        inventoryDto.upload(file);
    }

    @PostMapping("/")
    public String addQuantity(@RequestBody InventoryForm form) throws ApiException {
        return inventoryDto.addQuantity(form);
    }

    @GetMapping("/{barcode}")
    public InventoryForm getQuantity(@PathVariable("barcode") String barcode) throws ApiException {
        return inventoryDto.getQuantity(barcode);
    }

}

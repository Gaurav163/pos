package com.increff.pos.controller;


import com.increff.pos.dto.InventoryDto;
import com.increff.pos.model.ApiException;
import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api
@RestController
@RequestMapping("/api/inventories")
public class InventoryController {
    private final InventoryDto inventoryDto;

    @Autowired
    public InventoryController(InventoryDto inventoryDto) {
        this.inventoryDto = inventoryDto;
    }

    @ApiOperation(value = "Add quantity to multiple inventory using tsv file")
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public void upload(@RequestPart("file") MultipartFile file) throws ApiException {
        inventoryDto.upload(file);
    }

    @ApiOperation(value = "Add quantity to inventory for provided barcode")
    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public String addQuantity(@RequestBody InventoryForm form) throws ApiException {
        return inventoryDto.addQuantity(form);
    }

    @ApiOperation(value = "Get quantity in inventory for product with given barcode")
    @RequestMapping(value = "/{barcode}", method = RequestMethod.GET)
    public InventoryForm getQuantity(@PathVariable("barcode") String barcode) throws ApiException {
        return inventoryDto.getQuantity(barcode);
    }

    @ApiOperation(value = "Get all inventory")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<InventoryData> getAll() throws ApiException {
        return inventoryDto.getAllInventory();
    }
}

package com.increff.pos.dto;

import com.increff.pos.model.ApiException;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;

@Component
public class InventoryDto {

    private final InventoryService inventoryService;
    private final ProductService productService;

    @Autowired
    public InventoryDto(InventoryService inventoryService, ProductService productService) {
        this.inventoryService = inventoryService;
        this.productService = productService;
    }

    @Transactional(rollbackOn = ApiException.class)
    public void upload(MultipartFile file) throws ApiException {
        List<InventoryForm> forms = FileUploadUtil.convert(file, InventoryForm.class);
        for (InventoryForm form : forms) {
            addQuantity(form);
        }
    }

    public String addQuantity(InventoryForm form) throws ApiException {
        ProductPojo product = getProduct(form.getBarcode());
        InventoryPojo pojo = inventoryService.getById(product.getId());
        inventoryService.addQuantity(product.getId(), form.getQuantity());
        return "Quantity added to inventory of product with barcode : " + form.getBarcode();
    }

    public InventoryForm getQuantity(String barcode) throws ApiException {
        ProductPojo product = getProduct(barcode);
        if (product == null) {
            throw new ApiException("Invalid Barcode");
        }
        InventoryPojo pojo = inventoryService.getById(product.getId());
        InventoryForm form = new InventoryForm();
        form.setBarcode(barcode);
        if (pojo == null) {
            form.setQuantity(0L);
        } else {
            form.setQuantity(pojo.getQuantity());
        }
        return form;
    }

    private ProductPojo getProduct(String barcode) throws ApiException {
        return productService.getOneByParameter("barcode", barcode);
    }

}

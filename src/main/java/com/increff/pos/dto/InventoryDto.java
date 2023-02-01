package com.increff.pos.dto;

import com.increff.pos.model.ApiException;
import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static com.increff.pos.util.MapperUtil.mapper;

@Service
public class InventoryDto {
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private ProductService productService;


    @Transactional(rollbackOn = ApiException.class)
    public void upload(MultipartFile file) throws ApiException {
        List<InventoryForm> forms = FileUploadUtil.convert(file, InventoryForm.class);
        for (InventoryForm form : forms) {
            addQuantity(form);
        }
    }

    public List<InventoryData> getAllInventory() throws ApiException {
        List<ProductPojo> products = productService.getAll();
        List<InventoryData> inventoryDataList = new ArrayList<>();
        for (ProductPojo product : products) {
            InventoryData data = mapper(product, InventoryData.class);
            InventoryPojo inventoryPojo = inventoryService.getQuantity(product.getId());
            data.setQuantity(inventoryPojo.getQuantity());
            inventoryDataList.add(data);
        }
        return inventoryDataList;
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

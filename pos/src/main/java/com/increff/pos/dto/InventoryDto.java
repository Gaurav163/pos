package com.increff.pos.dto;

import com.increff.pos.model.ApiException;
import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.pojo.Brand;
import com.increff.pos.pojo.Inventory;
import com.increff.pos.pojo.Product;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static com.increff.pos.util.FormUtil.normalizeForm;
import static com.increff.pos.util.FormUtil.validateForm;
import static com.increff.pos.util.MapperUtil.mapper;

@Service
public class InventoryDto {
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private ProductService productService;
    @Autowired
    private BrandService brandService;

    @Transactional(rollbackOn = Exception.class)
    public void upload(MultipartFile file) throws ApiException {
        List<InventoryForm> forms = FileUploadUtil.convert(file, InventoryForm.class);
        if (forms.size() > 5000) {
            throw new ApiException("Files must not contains more than 5000 entries");
        }
        List<String> responses = new ArrayList<>();
        Long index = 0L;
        boolean error = false;
        for (InventoryForm form : forms) {
            index += 1;
            if (form == null) {
                responses.add("Row " + index + ": invalid row");
                error = true;
                continue;
            }
            try {
                increaseInventory(form);
            } catch (Exception e) {
                responses.add("Row " + index + ": Error  -> " + e.getMessage());
                error = true;
            }
        }
        if (error) {
            throw new ApiException(String.join("\r", responses));
        }
    }

    public List<InventoryData> getAllInventory() throws ApiException {
        List<Inventory> inventories = inventoryService.getAll();
        return extendData(inventories);
    }

    public InventoryData increaseInventory(InventoryForm form) throws ApiException {
        validateForm(form);
        normalizeForm(form);
        Long productId = getProductId(form.getBarcode());
        return extendData(inventoryService.increaseInventory(productId, form.getQuantity()));
    }

    public InventoryData updateInventory(InventoryForm form) throws ApiException {
        validateForm(form);
        normalizeForm(form);
        Long productId = getProductId(form.getBarcode());
        return extendData(inventoryService.updateInventory(productId, form.getQuantity()));
    }

    public InventoryData getByBarcode(String barcode) throws ApiException {
        Long productId = getProductId(barcode);
        return extendData(inventoryService.getById(productId));
    }


    protected Long getProductId(String barcode) throws ApiException {
        Product product = productService.getByParameter("barcode", barcode);
        if (product == null) {
            throw new ApiException("Invalid Barcode");
        }
        return product.getId();
    }

    protected List<InventoryData> extendData(List<Inventory> inventories) throws ApiException {
        List<InventoryData> dataList = new ArrayList<>();
        for (Inventory inventory : inventories) {
            dataList.add(extendData(inventory));
        }
        return dataList;
    }

    protected InventoryData extendData(Inventory inventory) throws ApiException {
        InventoryData data = mapper(inventory, InventoryData.class);
        Product product = productService.getById(inventory.getId());
        data.setName(product.getName());
        data.setBarcode(product.getBarcode());
        Brand brand = brandService.getById(product.getBrandId());
        data.setBrand(brand.getName());
        data.setCategory(brand.getCategory());
        return data;
    }

}

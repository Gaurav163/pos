package com.increff.pos.dto;

import com.increff.pos.model.ApiException;
import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.FileUploadUtil;
import com.increff.pos.util.MapperUtil;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Component
@Log4j
public class ProductDto {

    private final ProductService productService;
    private final BrandService brandService;
    private final InventoryService inventoryService;
    private static final String BRAND_ID = "brandId";

    @Autowired
    public ProductDto(ProductService productService, BrandService brandService, InventoryService inventoryService) {
        this.productService = productService;
        this.brandService = brandService;
        this.inventoryService = inventoryService;
    }

    @Transactional(rollbackOn = ApiException.class)
    public ProductData create(ProductForm form) throws ApiException {
        if (brandService.getById(form.getBrandId()) == null) {
            throw new ApiException("Brand Selected does not exits");
        }
        validate(form);
        ProductPojo productPojo = MapperUtil.mapper(form, ProductPojo.class);
        productService.create(productPojo);
        return extendData(productPojo);
    }

    public ProductData update(Long id, ProductForm form) throws ApiException {
        if (brandService.getById(form.getBrandId()) == null) {
            throw new ApiException("Brand Selected does not exits");
        }
        validate(form);
        ProductPojo productPojo = MapperUtil.mapper(form, ProductPojo.class);
        return extendData(productService.update(id, productPojo));
    }

    @Transactional(rollbackOn = ApiException.class)
    public List<String> upload(MultipartFile file) throws ApiException {
        List<ProductForm> forms = FileUploadUtil.convert(file, ProductForm.class);
        if (forms.size() > 5000) {
            throw new ApiException("Files should not contains more than 5000 Entries");
        }
        List<String> responses = new ArrayList<>();
        for (ProductForm form : forms) {
            try {
                if (brandService.getById(form.getBrandId()) == null) {
                    throw new ApiException("Brand Selected does not exits");
                }
                validate(form);
                ProductPojo productPojo = MapperUtil.mapper(form, ProductPojo.class);
                productService.create(productPojo);
                responses.add("Product Added Successfully");
            } catch (ApiException e) {
                throw new ApiException("Error : " + e.getMessage());
            }
        }
        return responses;
    }

    public List<ProductData> getAll() throws ApiException {
        log.info("Getting all products");
        List<ProductPojo> products = productService.getAll();
        return extendData(products);
    }

    public ProductData getById(Long id) throws ApiException {
        return extendData(productService.getById(id));
    }

    public ProductData getByBarcode(String barcode) throws ApiException {
        return extendData(productService.getOneByParameter("barcode", normalize(barcode)));
    }

    public List<ProductData> getByBrandCategory(Long brandId) throws ApiException {
        BrandPojo brand = brandService.getById(brandId);
        List<ProductPojo> products = productService.getListByParameter(BRAND_ID, brand.getId().toString());
        return extendData(products);
    }

    public List<ProductData> getByBrandName(String brandName) throws ApiException {

        List<BrandPojo> brands = brandService.getListByParameter("name", normalize(brandName));
        List<ProductPojo> products = new ArrayList<>();
        for (BrandPojo brand : brands) {
            List<ProductPojo> products2 = productService.getListByParameter(BRAND_ID, brand.getId().toString());
            products.addAll(products2);
        }
        return extendData(products);
    }

    public List<ProductData> getByBrandCategory(String brandCategory) throws ApiException {
        List<BrandPojo> brands = brandService.getListByParameter("category", normalize(brandCategory));
        List<ProductPojo> products = new ArrayList<>();
        for (BrandPojo brand : brands) {
            List<ProductPojo> products2 = productService.getListByParameter(BRAND_ID, brand.getId().toString());
            products.addAll(products2);
        }
        return extendData(products);
    }

    private List<ProductData> extendData(List<ProductPojo> products) throws ApiException {
        List<ProductData> data = new ArrayList<>();
        for (ProductPojo product : products) {
            data.add(extendData(product));
        }
        return data;
    }

    private ProductData extendData(ProductPojo product) throws ApiException {
        ProductData data = MapperUtil.mapper(product, ProductData.class);
        BrandPojo brand = brandService.getById(product.getBrandId());
        data.setBrandName(brand.getName());
        data.setBrandCategory(brand.getCategory());
        InventoryPojo inventory = inventoryService.getQuantity(data.getId());
        data.setQuantity(inventory.getQuantity());
        return data;
    }

    public static void validate(ProductForm form) throws ApiException {
        if (form.getName() == null || form.getBarcode() == null || form.getMrp() == null || form.getBrandId() == null) {
            throw new ApiException("Product Name, MRP, Barcode and BrandId are required.");
        }
        form.setName(normalize(form.getName()));
        form.setBarcode(normalize(form.getBarcode()));
        if (form.getName().isEmpty() || form.getBarcode().isEmpty()) {
            throw new ApiException("Product Name, MRP, Barcode and BrandId are required.");
        }
    }

    public static String normalize(String s) {
        return s == null ? null : s.toLowerCase().trim();
    }

}

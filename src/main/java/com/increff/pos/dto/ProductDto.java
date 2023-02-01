package com.increff.pos.dto;

import com.increff.pos.model.ApiException;
import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static com.increff.pos.util.FormUtil.*;
import static com.increff.pos.util.MapperUtil.mapper;

@Service
public class ProductDto {
    @Autowired
    private ProductService productService;
    @Autowired
    private BrandService brandService;

    private static final String BRAND_ID = "brandId";


    public ProductData create(ProductForm form) throws ApiException {
        if (brandService.getById(form.getBrandId()) == null) {
            throw new ApiException("Brand selected does not exist");
        }
        validateForm(form);
        normalizeForm(form);
        ProductPojo productPojo = mapper(form, ProductPojo.class);
        productService.create(productPojo);
        return extendData(productPojo);
    }

    public ProductData update(Long id, ProductForm form) throws ApiException {
        if (brandService.getById(form.getBrandId()) == null) {
            throw new ApiException("Brand selected does not exist");
        }
        validateForm(form);
        normalizeForm(form);
        ProductPojo productPojo = mapper(form, ProductPojo.class);
        return extendData(productService.update(id, productPojo));
    }

    @Transactional(rollbackFor = ApiException.class)
    public List<String> upload(MultipartFile file) throws ApiException {
        List<ProductForm> forms = FileUploadUtil.convert(file, ProductForm.class);
        if (forms.size() > 5000) {
            throw new ApiException("File should not contains more than 5000 entries");
        }
        List<String> responses = new ArrayList<>();
        for (ProductForm form : forms) {
            try {
                if (brandService.getById(form.getBrandId()) == null) {
                    throw new ApiException("Brand selected does not exist");
                }
                validateForm(form);
                normalizeForm(form);
                ProductPojo productPojo = mapper(form, ProductPojo.class);
                productService.create(productPojo);
                responses.add("Product added successfully");
            } catch (ApiException e) {
                throw new ApiException("Error : " + e.getMessage());
            }
        }
        return responses;
    }

    public List<ProductData> getAll() throws ApiException {
        List<ProductPojo> products = productService.getAll();
        return extendData(products);
    }

    public ProductData getById(Long id) throws ApiException {
        return extendData(productService.getById(id));
    }

    public ProductData getByBarcode(String barcode) throws ApiException {
        return extendData(productService.getOneByParameter("barcode", normalize(barcode)));
    }

    private List<ProductData> extendData(List<ProductPojo> products) throws ApiException {
        List<ProductData> data = new ArrayList<>();
        for (ProductPojo product : products) {
            data.add(extendData(product));
        }
        return data;
    }

    private ProductData extendData(ProductPojo product) throws ApiException {
        ProductData data = mapper(product, ProductData.class);
        BrandPojo brand = brandService.getById(product.getBrandId());
        data.setBrandName(brand.getName());
        data.setBrandCategory(brand.getCategory());
        return data;
    }

}

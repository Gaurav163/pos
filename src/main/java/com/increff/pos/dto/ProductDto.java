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
        ProductPojo productPojo = convertToPojo(form);
        productService.create(productPojo);
        return extendData(productPojo);
    }

    public ProductData update(Long id, ProductForm form) throws ApiException {
        return extendData(productService.update(id, convertToPojo(form)));
    }

    @Transactional(rollbackFor = Exception.class)
    public void upload(MultipartFile file) throws ApiException {
        List<ProductForm> forms = FileUploadUtil.convert(file, ProductForm.class);
        if (forms.size() > 5000) {
            throw new ApiException("File should not contains more than 5000 entries");
        }
        List<String> responses = new ArrayList<>();
        Long index = 0L;
        boolean error = false;
        for (ProductForm form : forms) {
            index += 1;
            if (form == null) {
                responses.add("Line " + index + ": invalid row");
                error = true;
                continue;
            }
            try {
                productService.create(convertToPojo(form));
                responses.add("Line " + index + ": All good");
            } catch (Exception e) {
                responses.add("Line " + index + ": Error while creating product -" + e.getMessage());
                error = true;
            }
        }

        if (error) {
            throw new ApiException(String.join("\n\r", responses));
        }
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
        data.setBrand(brand.getName());
        data.setCategory(brand.getCategory());
        return data;
    }

    private ProductPojo convertToPojo(ProductForm form) throws ApiException {
        validateForm(form);
        normalizeForm(form);
        BrandPojo existingBrand = brandService.getByNameAndCategory(form.getBrand(), form.getCategory());
        if (existingBrand == null) {
            throw new ApiException("Brand with name  '" + form.getBrand() + "' and category '" + form.getCategory() + "' does not exist");
        }
        ProductPojo productPojo = mapper(form, ProductPojo.class);
        productPojo.setBrandId(existingBrand.getId());
        return productPojo;

    }

}

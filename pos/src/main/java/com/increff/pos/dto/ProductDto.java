package com.increff.pos.dto;

import com.increff.pos.model.ApiException;
import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.pojo.Brand;
import com.increff.pos.pojo.Product;
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
    private static final String BRAND_ID = "brandId";
    @Autowired
    private ProductService productService;
    @Autowired
    private BrandService brandService;

    public ProductData create(ProductForm form) throws ApiException {
        Product product = convertToProduct(form);
        productService.create(product);
        return extendData(product);
    }

    public ProductData update(Long id, ProductForm form) throws ApiException {
        normalizeForm(form);
        Product product = mapper(form, Product.class);
        if (form.getBrand() != null && !form.getBrand().isEmpty() && form.getCategory() != null && !form.getCategory().isEmpty()) {
            Brand existingBrand = brandService.getByNameAndCategory(form.getBrand(), form.getCategory());
            if (existingBrand == null) {
                throw new ApiException("Brand with name  '" + form.getBrand() + "' and category '" + form.getCategory() + "' does not exist");
            }
            product.setBrandId(existingBrand.getId());
        }
        System.out.println(product.toString());
        return extendData(productService.update(id, product));
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
                responses.add("Row " + index + ": invalid row");
                error = true;
                continue;
            }
            try {
                productService.create(convertToProduct(form));
                responses.add("Row " + index + ": All good");
            } catch (Exception e) {
                responses.add("Row " + index + ": Error while creating product -" + e.getMessage());
                error = true;
            }
        }

        if (error) {
            throw new ApiException(String.join("\r", responses));
        }
    }

    public List<ProductData> getAll() throws ApiException {
        List<Product> products = productService.getAll();
        return extendData(products);
    }

    public ProductData getById(Long id) throws ApiException {
        return extendData(productService.getById(id));
    }

    public ProductData getByBarcode(String barcode) throws ApiException {
        return extendData(productService.getOneByParameter("barcode", normalize(barcode)));
    }

    private List<ProductData> extendData(List<Product> products) throws ApiException {
        List<ProductData> data = new ArrayList<>();
        for (Product product : products) {
            data.add(extendData(product));
        }
        return data;
    }

    private ProductData extendData(Product product) throws ApiException {
        ProductData data = mapper(product, ProductData.class);
        Brand brand = brandService.getById(product.getBrandId());
        data.setBrand(brand.getName());
        data.setCategory(brand.getCategory());
        return data;
    }

    private Product convertToProduct(ProductForm form) throws ApiException {
        validateForm(form);
        normalizeForm(form);
        Brand existingBrand = brandService.getByNameAndCategory(form.getBrand(), form.getCategory());
        if (existingBrand == null) {
            throw new ApiException("Brand with name  '" + form.getBrand() + "' and category '" + form.getCategory() + "' does not exist");
        }
        Product product = mapper(form, Product.class);
        product.setBrandId(existingBrand.getId());
        return product;

    }

}

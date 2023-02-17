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

import static com.increff.pos.util.FormUtil.normalizeForm;
import static com.increff.pos.util.FormUtil.validateForm;
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
                throw new ApiException("Invalid brand category");
            }
            product.setBrandId(existingBrand.getId());
        }
        return extendData(productService.update(id, product));
    }

    @Transactional(rollbackFor = Exception.class)
    public void upload(MultipartFile file) throws ApiException {
        List<ProductForm> forms = FileUploadUtil.convert(file, ProductForm.class);
        if (forms.size() > 5000) {
            throw new ApiException("File must not contains more than 5000 entries");
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
            } catch (Exception e) {
                responses.add("Row " + index + ": Error -" + e.getMessage());
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


    protected List<ProductData> extendData(List<Product> products) throws ApiException {
        List<ProductData> data = new ArrayList<>();
        for (Product product : products) {
            data.add(extendData(product));
        }
        return data;
    }

    protected ProductData extendData(Product product) throws ApiException {
        if (product == null) return null;
        ProductData data = mapper(product, ProductData.class);
        Brand brand = brandService.getById(product.getBrandId());
        data.setBrand(brand.getName());
        data.setCategory(brand.getCategory());
        return data;
    }

    protected Product convertToProduct(ProductForm form) throws ApiException {
        validateForm(form);
        normalizeForm(form);
        Brand existingBrand = brandService.getByNameAndCategory(form.getBrand(), form.getCategory());
        if (existingBrand == null) {
            throw new ApiException("Invalid brand category");
        }
        Product product = mapper(form, Product.class);
        product.setBrandId(existingBrand.getId());
        return product;
    }

}

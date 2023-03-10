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

import java.io.*;
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
        validateForm(form);
        Product product = convertToProduct(form);
        productService.create(product);
        return extendBrand(product);
    }

    public ProductData update(Long id, ProductForm form) throws ApiException {
        normalizeForm(form);
        validateForm(form);
        Product product = mapper(form, Product.class);
        if (form.getBrand() != null && !form.getBrand().isEmpty() && form.getCategory() != null && !form.getCategory().isEmpty()) {
            Brand existingBrand = brandService.getByNameAndCategory(form.getBrand(), form.getCategory());
            if (existingBrand == null) {
                throw new ApiException("Invalid brand category");
            }
            product.setBrandId(existingBrand.getId());
        }
        return extendBrand(productService.update(id, product));
    }

    @Transactional(rollbackFor = Exception.class)
    public void upload(MultipartFile file) throws ApiException, IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
        String fileHeader = reader.readLine();
        String sampleHeader = new BufferedReader(new FileReader(new File("src/main/resources/sample-product.tsv"))).readLine().trim();
        if (!sampleHeader.equals(fileHeader.trim())) {
            throw new ApiException("File columns not matched with sample file");
        }
        List<ProductForm> forms = FileUploadUtil.convert(file, ProductForm.class);
        if (forms.size() > 5000) {
            throw new ApiException("File must not contains more than 5000 entries");
        }
        List<String> responses = new ArrayList<>();
        responses.add(fileHeader + "\terrors");
        boolean error = false;
        for (ProductForm form : forms) {
            String currentRow = reader.readLine();
            if (form == null) {
                responses.add(currentRow + "\tinvalid row");
                error = true;
                continue;
            }
            try {
                create(form);
                responses.add(currentRow);
            } catch (Exception e) {
                responses.add(currentRow + "\t" + e.getMessage());
                error = true;
            }
        }
        if (error) {
            throw new ApiException(String.join("\r", responses));
        }
    }

    public List<ProductData> getAll() throws ApiException {
        List<Product> products = productService.getAll();
        return extendBrand(products);
    }

    protected List<ProductData> extendBrand(List<Product> products) throws ApiException {
        List<ProductData> data = new ArrayList<>();
        for (Product product : products) {
            data.add(extendBrand(product));
        }
        return data;
    }

    protected ProductData extendBrand(Product product) throws ApiException {
        if (product == null) return null;
        ProductData data = mapper(product, ProductData.class);
        Brand brand = brandService.getById(product.getBrandId());
        data.setBrand(brand.getName());
        data.setCategory(brand.getCategory());
        return data;
    }

    protected Product convertToProduct(ProductForm form) throws ApiException {
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

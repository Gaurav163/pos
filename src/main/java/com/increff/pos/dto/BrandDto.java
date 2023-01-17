package com.increff.pos.dto;


import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.BrandService;
import com.increff.pos.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class BrandDto {
    private final BrandService brandService;

    @Autowired
    public BrandDto(BrandService brandService) {
        this.brandService = brandService;
    }

    public static void normalize(BrandForm b) {
        b.setName(b.getName().toLowerCase().trim());
        b.setCategory(b.getCategory().toLowerCase().trim());
    }

    public BrandData save(BrandForm form) {
        if (form.getName() == null || form.getCategory() == null) {
            throw new IllegalStateException("Brand Name and Category are required to Update Brand.");
        }
        normalize(form);
        if (form.getName().isEmpty() || form.getCategory().isEmpty()) {
            throw new IllegalStateException("Brand Name and Category are required to Update Brand.");
        }
        BrandPojo brandPojo = new BrandPojo(form.getName(), form.getCategory());
        return pojoToBrand(brandService.save(brandPojo));
    }

    public List<BrandData> getAll() {
        return pojoToList(brandService.getAll());
    }

    public BrandData getById(Long id) {
        return pojoToBrand(brandService.getById(id));
    }

    public List<BrandData> getByName(String name) {
        return pojoToList(brandService.getByName(name));
    }

    public List<BrandData> getByCategory(String category) {
        return pojoToList(brandService.getByCategory(category));
    }

    public BrandData update(Long id, BrandForm form) {
        if (form.getName() == null || form.getCategory() == null) {
            throw new IllegalStateException("Brand Name and Category are required to Update Brand.");
        }
        normalize(form);
        if (form.getName().isEmpty() || form.getCategory().isEmpty()) {
            throw new IllegalStateException("Brand Name and Category are required to Update Brand.");
        }
        BrandPojo brandPojo = new BrandPojo(form.getName(), form.getCategory());
        brandPojo.setId(id);
        return pojoToBrand(brandService.update(id, brandPojo));
    }

    public List<String> upload(MultipartFile file) throws IOException {
        List<BrandForm> forms = FileUploadUtil.convert(file, BrandForm.class);
        List<String> responses = new ArrayList<>();
        int currentLine = 0;
        for (BrandForm form : forms) {
            if (form == null) responses.add("Invalid row");
            try {
                BrandPojo pojo = formToPojo(form);
                brandService.save(pojo);
                responses.add("Brand Added Successfully");
            } catch (Exception e) {
                responses.add("Error : " + e.getMessage());
            }
        }

        return responses;
    }

    private List<BrandData> pojoToList(List<BrandPojo> brandPojos) {
        List<BrandData> brandData = new ArrayList<>();
        for (BrandPojo b : brandPojos) {
            brandData.add(pojoToBrand(b));
        }
        return brandData;
    }

    private BrandData pojoToBrand(BrandPojo b) {
        return new BrandData(b.getId(), b.getName(), b.getCategory());
    }

    private BrandPojo formToPojo(BrandForm b) {
        return new BrandPojo(b.getName(), b.getCategory());
    }


}

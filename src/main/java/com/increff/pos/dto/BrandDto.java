package com.increff.pos.dto;


import com.increff.pos.model.ApiException;
import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.BrandService;
import com.increff.pos.util.FileUploadUtil;
import com.increff.pos.util.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Component
public class BrandDto {
    private final BrandService brandService;

    @Autowired
    public BrandDto(BrandService brandService) {
        this.brandService = brandService;
    }

    public BrandData create(BrandForm form) throws ApiException {
        validate(form);
        BrandPojo brandPojo = MapperUtil.mapper(form, BrandPojo.class);
        return MapperUtil.mapper(brandService.create(brandPojo), BrandData.class);
    }

    public List<BrandData> getAll() throws ApiException {
        return MapperUtil.mapper(brandService.getAll(), BrandData.class);
    }

    public BrandData getById(Long id) throws ApiException {
        BrandPojo pojo = brandService.getById(id);
        if (pojo == null) {
            throw new ApiException("Invalid ID");
        } else {
            return MapperUtil.mapper(brandService.getById(id), BrandData.class);
        }
    }

    public List<BrandData> getByName(String name) throws ApiException {
        return MapperUtil.mapper(brandService.getListByParameter("name", normalize(name)), BrandData.class);
    }

    public List<BrandData> getByCategory(String category) throws ApiException {
        return MapperUtil.mapper(brandService.getListByParameter("category", normalize(category)), BrandData.class);
    }

    public BrandData update(Long id, BrandForm form) throws ApiException {
        validate(form);
        BrandPojo brandPojo = MapperUtil.mapper(form, BrandPojo.class);
        brandPojo = brandService.update(id, brandPojo);
        return MapperUtil.mapper(brandPojo, BrandData.class);
    }

    @Transactional(rollbackOn = ApiException.class)
    public List<String> upload(MultipartFile file) throws ApiException {
        List<BrandForm> forms = FileUploadUtil.convert(file, BrandForm.class);
        List<String> responses = new ArrayList<>();

        for (BrandForm form : forms) {
            try {
                validate(form);
                BrandPojo pojo = MapperUtil.mapper(form, BrandPojo.class);
                brandService.create(pojo);
                responses.add("Brand Added Successfully");
            } catch (ApiException e) {
                throw new ApiException("Error : " + e.getMessage());
            }
        }

        return responses;
    }

    private static void validate(BrandForm form) throws ApiException {
        if (form.getName() == null || form.getCategory() == null) {
            throw new ApiException("Brand Name and Category are required.");
        }
        form.setName(normalize(form.getName()));
        form.setCategory(normalize(form.getCategory()));
        if (form.getName().isEmpty() || form.getCategory().isEmpty()) {
            throw new ApiException("Brand Name and Category are required.");
        }
    }

    private static String normalize(String s) {
        return s == null ? null : s.toLowerCase().trim();
    }


}

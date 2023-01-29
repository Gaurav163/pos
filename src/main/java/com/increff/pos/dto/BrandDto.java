package com.increff.pos.dto;


import com.increff.pos.model.ApiException;
import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static com.increff.pos.util.FileUploadUtil.convert;
import static com.increff.pos.util.FormUtil.*;
import static com.increff.pos.util.MapperUtil.mapper;

@Component
public class BrandDto {
    @Autowired
    private BrandService brandService;

    public BrandData create(BrandForm form) throws ApiException {
        validateForm(form);
        normalizeForm(form);
        BrandPojo brandPojo = mapper(form, BrandPojo.class);
        return mapper(brandService.create(brandPojo), BrandData.class);
    }

    public List<BrandData> getAll() throws ApiException {
        return mapper(brandService.getAll(), BrandData.class);
    }

    public BrandData getById(Long id) throws ApiException {
        BrandPojo pojo = brandService.getById(id);
        if (pojo == null) {
            throw new ApiException("Invalid brand ID");
        } else {
            return mapper(brandService.getById(id), BrandData.class);
        }
    }

    public List<BrandData> getByName(String name) throws ApiException {
        return mapper(brandService.getListByParameter("name", normalize(name)), BrandData.class);
    }

    public List<BrandData> getByCategory(String category) throws ApiException {
        return mapper(brandService.getListByParameter("category", normalize(category)), BrandData.class);
    }

    public BrandData update(Long id, BrandForm form) throws ApiException {
        normalizeForm(form);
        BrandPojo brandPojo = mapper(form, BrandPojo.class);
        return mapper(brandService.update(id, brandPojo), BrandData.class);
    }

    @Transactional(rollbackFor = ApiException.class)
    public List<String> upload(MultipartFile file) throws ApiException {
        List<BrandForm> forms = convert(file, BrandForm.class);
        if (forms.size() > 5000) {
            throw new ApiException("Files should not contains more than 5000 entries");
        }
        List<String> responses = new ArrayList<>();
        for (BrandForm form : forms) {
            try {
                validateForm(form);
                normalizeForm(form);
                BrandPojo pojo = mapper(form, BrandPojo.class);
                brandService.create(pojo);
                responses.add("Brand added successfully");
            } catch (ApiException e) {
                throw new ApiException("Error : " + e.getMessage());
            }
        }
        return responses;
    }

}

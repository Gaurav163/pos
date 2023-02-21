package com.increff.pos.dto;


import com.increff.pos.model.ApiException;
import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.pojo.Brand;
import com.increff.pos.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static com.increff.pos.util.FileUploadUtil.convert;
import static com.increff.pos.util.FormUtil.normalizeForm;
import static com.increff.pos.util.FormUtil.validateForm;
import static com.increff.pos.util.MapperUtil.mapper;

@Service
public class BrandDto {
    @Autowired
    private BrandService brandService;

    public BrandData create(BrandForm form) throws ApiException {
        validateForm(form);
        normalizeForm(form);
        Brand brand = mapper(form, Brand.class);
        return mapper(brandService.create(brand), BrandData.class);
    }

    public List<BrandData> getAll() throws ApiException {
        return mapper(brandService.getAll(), BrandData.class);
    }

    public BrandData getById(Long id) throws ApiException {
        Brand brand = brandService.getById(id);
        if (brand == null) {
            return null;
        } else {
            return mapper(brandService.getById(id), BrandData.class);
        }
    }

    public BrandData update(Long id, BrandForm form) throws ApiException {
        normalizeForm(form);
        Brand brand = mapper(form, Brand.class);
        return mapper(brandService.update(id, brand), BrandData.class);
    }

    @Transactional(rollbackFor = Exception.class)
    public void upload(MultipartFile file) throws ApiException {
        List<BrandForm> forms = convert(file, BrandForm.class);
        if (forms.size() > 5000) {
            throw new ApiException("Files must not contains more than 5000 entries");
        }
        List<String> responses = new ArrayList<>();
        Long index = 0L;
        boolean error = false;
        for (BrandForm form : forms) {
            index += 1;
            if (form == null) {
                responses.add("Row " + index + ": invalid row");
                error = true;
                continue;
            }
            try {
                create(form);
            } catch (Exception e) {
                responses.add("Row " + index + ": Error -" + e.getMessage());
                error = true;
            }
        }
        if (error) {
            throw new ApiException(String.join("\r", responses));
        }
    }

}

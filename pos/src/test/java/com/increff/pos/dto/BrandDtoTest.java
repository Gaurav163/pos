package com.increff.pos.dto;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.dao.BrandDao;
import com.increff.pos.helper.BrandHelper;
import com.increff.pos.model.ApiException;
import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.pojo.Brand;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.Assert.*;

public class BrandDtoTest extends AbstractUnitTest {

    @Autowired
    private BrandDto brandDto;

    @Autowired
    private BrandDao brandDao;

    @Autowired
    private BrandHelper helper;


    @Test
    public void testCreate() throws ApiException {
        BrandForm form = helper.getBrandForm("nAme6 ", "  CATeGory6");
        BrandData data = brandDto.create(form);
        assertNotNull(data);
        assertEquals("name6", data.getName());
        assertEquals("category6", data.getCategory());
    }

    @Test(expected = ApiException.class)
    public void testCreateEmpty() throws ApiException {
        BrandForm form = helper.getBrandForm("nAme6 ", "   ");
        brandDto.create(form);
        brandDao.getByParameter("name", "name6");
    }

    @Test(expected = ApiException.class)
    public void validateNull() throws ApiException {
        BrandForm form = helper.getBrandForm("nAme6 ", null);
        brandDto.create(form);
        brandDao.getByParameter("name", "name6");
    }

    @Test
    public void getAll() throws ApiException {
        helper.generateBrands(5);
        List<BrandData> dataList = brandDto.getAll();
        assertEquals(5, dataList.size());
    }


    @Test
    public void testGetById() throws ApiException {
        Brand brand = helper.createBrand("brand1", "category1");
        BrandData data = brandDto.getById(brand.getId());
        assertNotNull(data);
        assertEquals(brand.getName(), data.getName());
        assertEquals(brand.getCategory(), data.getCategory());
    }

    @Test
    public void testGetByInvalidId() throws ApiException {
        Brand brand = helper.createBrand("brand1", "category1");
        BrandData data = brandDto.getById(brand.getId() + 10);
        assertNull(data);
    }

    @Test
    public void testUpload() throws ApiException, IOException {
        MultipartFile file = new MockMultipartFile("brands.tsv",
                Files.newInputStream(new File("src/test/resources/brands.tsv").toPath()));
        brandDto.upload(file);
        assertEquals(5, brandDto.getAll().size());
    }

    @Test(expected = ApiException.class)
    public void testUploadDuplicate() throws IOException, ApiException {
        MultipartFile file = new MockMultipartFile("brands.tsv",
                Files.newInputStream(new File("src/test/resources/brands.tsv").toPath()));
        brandDto.upload(file);
        brandDto.upload(file);
    }

    @Test
    public void update() throws ApiException {
        Brand brand = helper.createBrand("brand1", "category1");
        BrandForm form = helper.getBrandForm("brand13", "category12");
        BrandData data = brandDto.update(brand.getId(), form);
        assertEquals(form.getName(), data.getName());
        assertEquals(form.getCategory(), data.getCategory());
    }


}

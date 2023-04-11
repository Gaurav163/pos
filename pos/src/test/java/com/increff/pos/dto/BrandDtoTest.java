package com.increff.pos.dto;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.TestHelper;
import com.increff.pos.dao.BrandDao;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BrandDtoTest extends AbstractUnitTest {

    @Autowired
    private BrandDto brandDto;

    @Autowired
    private BrandDao brandDao;

    @Autowired
    private TestHelper testHelper;


    @Test
    public void testCreate() throws ApiException {
        BrandForm form = testHelper.getBrandForm("nAme6 ", "  CATeGory6");
        BrandData data = brandDto.create(form);
        assertNotNull(data);
        assertEquals("name6", data.getName());
        assertEquals("category6", data.getCategory());
    }

    @Test(expected = ApiException.class)
    public void testCreateEmpty() throws ApiException {
        BrandForm form = testHelper.getBrandForm("nAme6 ", "   ");
        brandDto.create(form);
    }

    @Test(expected = ApiException.class)
    public void validateNull() throws ApiException {
        BrandForm form = testHelper.getBrandForm("nAme6 ", null);
        brandDto.create(form);
    }

    @Test
    public void getAll() throws ApiException {
        testHelper.generateBrands(5);
        List<BrandData> dataList = brandDto.getAll();
        assertEquals(5, dataList.size());
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
        Brand brand = testHelper.createBrand("brand1", "category1");
        BrandForm form = testHelper.getBrandForm("brand13", "category12");
        BrandData data = brandDto.update(brand.getId(), form);
        assertEquals(form.getName(), data.getName());
        assertEquals(form.getCategory(), data.getCategory());
    }


}

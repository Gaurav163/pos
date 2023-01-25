package com.increff.pos.dto;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.model.ApiException;
import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import org.junit.Before;
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

    @Before
    public void generateData() throws ApiException {
        createForm(" NamE1  ", " CateGoRY1 ");
        createForm(" namE2  ", "CateGoRY2 ");
        createForm("NAME3", " CateGoRY3");
        createForm(" NamE4  ", " CATEGORY4 ");
        createForm("name5", " CateGoRY5 ");
    }

    private void createForm(String name, String category) throws ApiException {
        BrandForm form = new BrandForm();
        form.setName(name);
        form.setCategory(category);
        brandDto.create(form);
    }

    @Test
    public void create() throws ApiException {
        BrandForm form = new BrandForm();
        form.setName("NAME6  ");
        form.setCategory("  CatEGORY6");
        BrandData data = brandDto.create(form);
        assertNotNull(data);
        assertEquals("name6", data.getName());
        assertEquals("category6", data.getCategory());
    }

    @Test(expected = ApiException.class)
    public void validateNull() throws ApiException {
        BrandForm form = new BrandForm();
        form.setName("NAME6  ");
        brandDto.create(form);
    }

    @Test(expected = ApiException.class)
    public void validateEmpty() throws ApiException {
        BrandForm form = new BrandForm();
        form.setName("NAME6  ");
        form.setCategory("   ");
        brandDto.create(form);
    }

    @Test
    public void getAll() throws ApiException {
        List<BrandData> list = brandDto.getAll();
        assertEquals(5, list.size());
    }

    @Test
    public void getByName() throws ApiException {
        List<BrandData> list = brandDto.getByName("name1");
        assertEquals(1, list.size());
    }

    @Test
    public void getByCategory() throws ApiException {
        List<BrandData> list = brandDto.getByCategory("category1");
        assertEquals(1, list.size());
    }

    @Test
    public void getById() throws ApiException {
        List<BrandData> list = brandDto.getAll();
        assertEquals(5, list.size());
        for (BrandData brand : list) {
            BrandData data = brandDto.getById(brand.getId());
            assertNotNull(data);
        }
    }

    @Test(expected = ApiException.class)
    public void getByIdInvalid() throws ApiException {
        List<BrandData> list = brandDto.getAll();
        assertEquals(5, list.size());
        brandDto.getById(list.get(4).getId() + 1);
    }

    @Test
    public void update() throws ApiException {
        assertTrue(brandDto.getByName("name11").isEmpty());
        List<BrandData> list = brandDto.getByName("name1");
        assertTrue(list.size() > 0);
        BrandForm form = new BrandForm();
        form.setCategory("category11");
        form.setName("name11");
        brandDto.update(list.get(0).getId(), form);
        assertFalse(brandDto.getByName("name11").isEmpty());
    }

    @Test
    public void upload() throws ApiException, IOException {
        MultipartFile file = new MockMultipartFile("brands.tsv",
                Files.newInputStream(new File("src/test/resources/brands.tsv").toPath()));
        brandDto.upload(file);
        assertTrue(brandDto.getAll().size() > 5);
    }

    @Test(expected = ApiException.class)
    public void uploadDuplicate() throws IOException, ApiException {
        MultipartFile file = new MockMultipartFile("brands.tsv",
                Files.newInputStream(new File("src/test/resources/brands.tsv").toPath()));
        brandDto.upload(file);
        brandDto.upload(file);
    }


}

package com.increff.pos.dto;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.Helper;
import com.increff.pos.dao.ProductDao;
import com.increff.pos.model.ApiException;
import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.pojo.Brand;
import com.increff.pos.pojo.Product;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.Assert.*;

public class ProductDtoTest extends AbstractUnitTest {
    @Autowired
    private ProductDto productDto;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private Helper helper;

    @Test
    public void testCovertToProduct() throws ApiException {
        helper.createBrand("brand", "category");
        ProductForm form = helper.createProductForm("prodUCT  ", "barCoDe", "Brand", "   caTEgory", 99.90);
        Product product = productDto.convertToProduct(form);
        assertNotNull(product);
        assertEquals("product", product.getName());
        assertEquals("barcode", product.getBarcode());
    }

    @Test(expected = ApiException.class)
    public void testCovertToProductInvalidBrand() throws ApiException {
        ProductForm form = helper.createProductForm("prodUCT  ", "barCoDe", "Brand", "   caTEgory", 99.90);
        Product product = productDto.convertToProduct(form);
    }

    @Test
    public void testExtendData() throws ApiException {
        Brand brand = helper.createBrand("brand", "category");
        Product product = helper.createProduct("product1", "barcode1", brand.getId(), 99.90);
        Product savedProduct = productDao.getByParameter("barcode", "barcode1");
        ProductData data = productDto.extendBrand(savedProduct);
        assertNotNull(data);
        assertEquals(brand.getName(), data.getBrand());
        assertEquals(brand.getCategory(), data.getCategory());
    }

    @Test
    public void testExtendDataList() throws ApiException {
        Brand brand = helper.createBrand("brand", "category");
        Brand brand1 = helper.createBrand("brand1", "category1");
        helper.createProduct("product1", "barcode1", brand.getId(), 99.9);
        helper.createProduct("product2", "barcode2", brand.getId(), 99.9);
        helper.createProduct("product3", "barcode3", brand.getId(), 99.9);
        helper.createProduct("product4", "barcode4", brand1.getId(), 99.9);
        List<ProductData> dataList = productDto.extendBrand(productDao.getAll());
        assertEquals(4, dataList.size());
    }

    @Test
    public void testCreate() throws ApiException {
        Brand brand = helper.createBrand("brand1", "category1");
        ProductForm form = helper.createProductForm("product1", "BARCoDE1", "BRand1", "category1", 99.00);
        ProductData data = productDto.create(form);
        assertNotNull(data);
        Product product = productDao.getByParameter("barcode", "barcode1");
        assertNotNull(product);
        assertEquals(brand.getId(), product.getBrandId());
    }

    @Test
    public void testGetAll() throws ApiException {
        Brand brand = helper.createBrand("brand1", "category1");
        helper.createProduct("product1", "barcode1", brand.getId(), 99.0);
        helper.createProduct("product2", "barcode2", brand.getId(), 99.0);
        helper.createProduct("product3", "barcode3", brand.getId(), 99.0);
        helper.createProduct("product4", "barcode4", brand.getId(), 99.0);
        helper.createProduct("product5", "barcode5", brand.getId(), 99.0);
        List<ProductData> dataList = productDto.getAll();
        assertEquals(5, dataList.size());
    }

    @Test
    public void testGetById() throws ApiException {
        Brand brand1 = helper.createBrand("brand1", "category1");
        Product product = helper.createProduct("product1", "BARCoDE1", brand1.getId(), 99.00);
        ProductData data = productDto.getById(product.getId());
        ProductData dummyData = productDto.getById(product.getId() + 10);
        assertNotNull(data);
        assertNull(dummyData);
        assertEquals(data.getBrand(), brand1.getName());
        assertEquals(data.getCategory(), brand1.getCategory());
    }

    @Test
    public void testUpdate() throws ApiException {
        Brand brand1 = helper.createBrand("brand1", "category1");
        Brand brand2 = helper.createBrand("brand2", "category2");
        Product product = helper.createProduct("product1", "BARCoDE1", brand1.getId(), 99.00);
        ProductForm form = helper.createProductForm("product1", "BARCoDE2", "BRand2", "category2", 100.00);
        productDto.update(product.getId(), form);
        Product savedProduct = productDao.getByParameter("barcode", "barcode2");
        assertNotNull(savedProduct);
        assertEquals(brand2.getId(), savedProduct.getBrandId());
        assertEquals(Double.valueOf(100.0), savedProduct.getMrp());
    }

    @Test(expected = ApiException.class)
    public void testUpdateInvalidBrand() throws ApiException {
        Brand brand1 = helper.createBrand("brand1", "category1");
        Product product = helper.createProduct("product1", "BARCoDE1", brand1.getId(), 99.00);
        ProductForm form = helper.createProductForm("product1", "BARCoDE2", "BRand2", "category2", 100.00);
        productDto.update(product.getId(), form);
        productDao.getByParameter("barcode", "barcode2");
    }

    @Test
    public void testUpload() throws ApiException, IOException {
        helper.createBrand("brand1", "cate1");
        MultipartFile file = new MockMultipartFile("product.tsv",
                Files.newInputStream(new File("src/test/resources/product.tsv").toPath()));
        productDto.upload(file);
        assertEquals(5, productDao.getAll().size());
    }

    @Test(expected = ApiException.class)
    public void testUploadDuplicate() throws IOException, ApiException {
        helper.createBrand("brand1", "category1");
        MultipartFile file = new MockMultipartFile("product.tsv",
                Files.newInputStream(new File("src/test/resources/product.tsv").toPath()));
        productDto.upload(file);
        productDto.upload(file);
    }

    @Test(expected = ApiException.class)
    public void testUploadInvalidBrand() throws IOException, ApiException {
        MultipartFile file = new MockMultipartFile("product.tsv",
                Files.newInputStream(new File("src/test/resources/product.tsv").toPath()));
        productDto.upload(file);
    }

}

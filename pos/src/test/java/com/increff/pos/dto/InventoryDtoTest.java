package com.increff.pos.dto;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.TestHelper;
import com.increff.pos.dao.InventoryDao;
import com.increff.pos.model.ApiException;
import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.pojo.Brand;
import com.increff.pos.pojo.Inventory;
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

public class InventoryDtoTest extends AbstractUnitTest {
    @Autowired
    private InventoryDto inventoryDto;
    @Autowired
    private InventoryDao inventoryDao;
    @Autowired
    private TestHelper testHelper;

    @Test
    public void testExtendData() throws ApiException {
        Brand brand = testHelper.createBrand("brand1", "category1");
        Product product = testHelper.createProduct("product1", "barcode1", brand.getId(), 99.9);
        Inventory inventory = testHelper.getInventory(product.getId(), 10L);
        InventoryData data = inventoryDto.extendData(inventory);
        assertNotNull(data);
        assertEquals(brand.getName(), data.getBrand());
        assertEquals(brand.getCategory(), data.getCategory());
        assertEquals(product.getBarcode(), data.getBarcode());
    }

    @Test
    public void testGetProductId() throws ApiException {
        Brand brand = testHelper.createBrand("brand1", "category1");
        Product product = testHelper.createProduct("product1", "barcode1", brand.getId(), 99.9);
        Long productId = inventoryDto.getProductId("barcode1");
        assertNotNull(productId);
        assertEquals(product.getId(), productId);
    }

    @Test(expected = ApiException.class)
    public void testGetInvalidProductId() throws ApiException {
        inventoryDto.getProductId("barcode1");
    }

    @Test
    public void testGetAll() throws ApiException {
        Brand brand = testHelper.createBrand("brand1", "category1");
        Product product1 = testHelper.createProduct("product1", "barcode1", brand.getId(), 99.9);
        Product product2 = testHelper.createProduct("product2", "barcode2", brand.getId(), 99.9);
        Product product3 = testHelper.createProduct("product3", "barcode3", brand.getId(), 99.9);
        testHelper.createInventory(product1.getId(), 5L);
        testHelper.createInventory(product2.getId(), 5L);
        testHelper.createInventory(product3.getId(), 5L);
        List<InventoryData> dataList = inventoryDto.getAllInventory();
        assertEquals(3, dataList.size());

    }

    @Test
    public void testUpdate() throws ApiException {
        Brand brand = testHelper.createBrand("brand1", "category1");
        Product product = testHelper.createProduct("product1", "barcode1", brand.getId(), 99.9);
        testHelper.createInventory(product.getId(), 10L);
        Inventory inventory = inventoryDao.getById(product.getId());
        assertEquals(Long.valueOf(10L), inventory.getQuantity());
        InventoryForm form = testHelper.createInventoryForm(product.getBarcode(), 24L);
        inventoryDto.updateInventory(form);
        Inventory savedInventory = inventoryDao.getById(product.getId());
        assertEquals(Long.valueOf(24L), savedInventory.getQuantity());
    }

    @Test
    public void testIncreaseInventory() throws ApiException {
        Brand brand = testHelper.createBrand("brand1", "category1");
        Product product = testHelper.createProduct("product1", "barcode1", brand.getId(), 99.9);
        Inventory inventory = inventoryDao.getById(product.getId());
        assertNull(inventory);
        InventoryForm inventoryForm = testHelper.createInventoryForm("barcode1", 10L);
        inventoryDto.increaseInventory(inventoryForm);
        Inventory inventory2 = inventoryDao.getById(product.getId());
        assertNotNull(inventory2);
        assertEquals(Long.valueOf(10), inventory2.getQuantity());
    }

    @Test(expected = ApiException.class)
    public void testIncreaseInventoryInvalidBarcode() throws ApiException {
        InventoryForm form = testHelper.createInventoryForm("barcode1", 10L);
        inventoryDto.increaseInventory(form);
        inventoryDao.getAll();
    }

    @Test
    public void testUpload() throws ApiException, IOException {
        Brand brand = testHelper.createBrand("brand", "category");
        testHelper.createProduct("product1", "barcode1", brand.getId(), 90D);
        testHelper.createProduct("product2", "barcode2", brand.getId(), 90D);
        testHelper.createProduct("product3", "barcode3", brand.getId(), 90D);
        testHelper.createProduct("product4", "barcode4", brand.getId(), 90D);
        MultipartFile file = new MockMultipartFile("inventory.tsv",
                Files.newInputStream(new File("src/test/resources/inventory.tsv").toPath()));
        inventoryDto.upload(file);
        assertEquals(4, inventoryDao.getAll().size());
    }

    @Test(expected = ApiException.class)
    public void testUploadInvalidBarcode() throws ApiException, IOException {
        MultipartFile file = new MockMultipartFile("inventory.tsv",
                Files.newInputStream(new File("src/test/resources/inventory.tsv").toPath()));
        inventoryDto.upload(file);
    }


}

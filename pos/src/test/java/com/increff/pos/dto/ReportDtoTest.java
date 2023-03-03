package com.increff.pos.dto;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.TestHelper;
import com.increff.pos.model.*;
import com.increff.pos.pojo.Brand;
import com.increff.pos.pojo.Order;
import com.increff.pos.pojo.Product;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ReportDtoTest extends AbstractUnitTest {
    @Autowired
    private ReportDto reportDto;
    @Autowired
    private TestHelper testHelper;

    @Test
    public void testBandReport() throws ApiException {
        testHelper.createBrand("brand1", "cate1");
        testHelper.createBrand("brand2", "cate2");
        testHelper.createBrand("brand3", "cate3");
        testHelper.createBrand("brand4", "cate4");
        testHelper.createBrand("brand5", "cate5");
        List<BrandForm> report = reportDto.getBrandReport();
        assertEquals(5, report.size());
    }

    @Test
    public void testInventoryReport() throws ApiException {
        Brand brand1 = testHelper.createBrand("brand1", "cate1");
        Brand brand2 = testHelper.createBrand("brand1", "cate2");
        Brand brand3 = testHelper.createBrand("brand2", "cate1");
        Brand brand4 = testHelper.createBrand("brand2", "cate2");
        Product product1 = testHelper.createProduct("product1", "barcode1", brand1.getId(), 44.50);
        Product product2 = testHelper.createProduct("product2", "barcode2", brand1.getId(), 144.50);
        Product product3 = testHelper.createProduct("product3", "barcode3", brand3.getId(), 424.50);
        Product product4 = testHelper.createProduct("product4", "barcode4", brand2.getId(), 442.50);
        Product product5 = testHelper.createProduct("product5", "barcode5", brand4.getId(), 449.50);
        Product product6 = testHelper.createProduct("product6", "barcode6", brand4.getId(), 244.50);
        testHelper.createInventory(product1.getId(), 100L);
        testHelper.createInventory(product2.getId(), 100L);
        testHelper.createInventory(product3.getId(), 100L);
        testHelper.createInventory(product4.getId(), 100L);
        testHelper.createInventory(product5.getId(), 100L);
        testHelper.createInventory(product6.getId(), 100L);

        List<InventoryReportData> data = reportDto.getInventoryReport();
        assertEquals(4, data.size());
        List<Long> actualData = new ArrayList<>();
        actualData.add(200L);
        actualData.add(100L);
        actualData.add(100L);
        actualData.add(200L);
        for (int i = 0; i < 4; i++) {
            assertEquals(data.get(i).getQuantity(), actualData.get(i));
        }
    }

    @Test
    public void testDailyReport() throws ApiException {
        testHelper.createDailyReport(ZonedDateTime.now().minusDays(1).truncatedTo(ChronoUnit.DAYS), 10020.0, 1200L, 113L);
        testHelper.createDailyReport(ZonedDateTime.now().minusDays(2).truncatedTo(ChronoUnit.DAYS), 10020.0, 180L, 43L);
        testHelper.createDailyReport(ZonedDateTime.now().minusDays(3).truncatedTo(ChronoUnit.DAYS), 11200.0, 100L, 15L);
        testHelper.createDailyReport(ZonedDateTime.now().minusDays(4).truncatedTo(ChronoUnit.DAYS), 20200.0, 1300L, 103L);
        testHelper.createDailyReport(ZonedDateTime.now().minusDays(5).truncatedTo(ChronoUnit.DAYS), 32200.0, 3000L, 212L);
        List<DailyReportData> dataList = reportDto.getDailyReport(ZonedDateTime.now().minusDays(5).truncatedTo(ChronoUnit.DAYS).toOffsetDateTime().toString(),
                ZonedDateTime.now().minusDays(2).truncatedTo(ChronoUnit.DAYS).toOffsetDateTime().toString());
        assertEquals(4, dataList.size());

    }

    @Test
    public void testSalesReport() throws ApiException {
        Brand brand1 = testHelper.createBrand("brand1", "cate1");
        Brand brand2 = testHelper.createBrand("brand1", "cate2");
        Brand brand3 = testHelper.createBrand("brand2", "cate1");
        Brand brand4 = testHelper.createBrand("brand2", "cate2");
        Product product1 = testHelper.createProduct("product1", "barcode1", brand1.getId(), 44.50);
        Product product2 = testHelper.createProduct("product2", "barcode2", brand1.getId(), 144.50);
        Product product3 = testHelper.createProduct("product3", "barcode3", brand3.getId(), 424.50);
        Product product4 = testHelper.createProduct("product4", "barcode4", brand2.getId(), 442.50);
        Product product5 = testHelper.createProduct("product5", "barcode5", brand4.getId(), 449.50);
        Product product6 = testHelper.createProduct("product6", "barcode6", brand4.getId(), 244.50);
        testHelper.createInventory(product1.getId(), 100L);
        testHelper.createInventory(product2.getId(), 100L);
        testHelper.createInventory(product3.getId(), 100L);
        testHelper.createInventory(product4.getId(), 100L);
        testHelper.createInventory(product5.getId(), 100L);
        testHelper.createInventory(product6.getId(), 100L);
        Order order1 = testHelper.createOrder();
        Order order2 = testHelper.createOrder(ZonedDateTime.now().minusDays(1).plusMinutes(1));
        Order order3 = testHelper.createOrder(ZonedDateTime.now().minusDays(2).plusMinutes(1));
        Order order4 = testHelper.createOrder(ZonedDateTime.now().minusDays(3).plusMinutes(1));
        testHelper.createOrderItem(order1.getId(), product1.getId(), 5L, 200.0);
        testHelper.createOrderItem(order1.getId(), product3.getId(), 5L, 200.0);
        testHelper.createOrderItem(order1.getId(), product4.getId(), 5L, 200.0);
        testHelper.createOrderItem(order2.getId(), product1.getId(), 5L, 200.0);
        testHelper.createOrderItem(order2.getId(), product5.getId(), 5L, 200.0);
        testHelper.createOrderItem(order2.getId(), product6.getId(), 5L, 200.0);
        testHelper.createOrderItem(order3.getId(), product6.getId(), 5L, 200.0);
        testHelper.createOrderItem(order3.getId(), product2.getId(), 5L, 200.0);
        testHelper.createOrderItem(order3.getId(), product4.getId(), 5L, 200.0);
        testHelper.createOrderItem(order3.getId(), product5.getId(), 5L, 200.0);
        testHelper.createOrderItem(order4.getId(), product4.getId(), 5L, 200.0);
        testHelper.createOrderItem(order4.getId(), product3.getId(), 5L, 200.0);
        testHelper.createOrderItem(order4.getId(), product5.getId(), 5L, 200.0);
        testHelper.createOrderItem(order4.getId(), product1.getId(), 5L, 200.0);

        ReportForm form = new ReportForm();
        form.setEndDate(ZonedDateTime.now().toOffsetDateTime().toString());
        form.setStartDate(ZonedDateTime.now().minusDays(3).toOffsetDateTime().toString());
        List<ReportData> data = reportDto.getSalesReport(form);
        assertEquals(4, data.size());
        form.setBrand("brand1");
        List<ReportData> dataBrand = reportDto.getSalesReport(form);
        assertEquals(2, dataBrand.size());
        form.setCategory("cate2");
        List<ReportData> dataBrandAndCategory = reportDto.getSalesReport(form);
        assertEquals(1, dataBrandAndCategory.size());
    }


}

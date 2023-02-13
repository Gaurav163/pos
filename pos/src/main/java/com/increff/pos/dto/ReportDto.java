package com.increff.pos.dto;

import com.increff.pos.model.*;
import com.increff.pos.pojo.*;
import com.increff.pos.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.increff.pos.util.FormUtil.normalizeForm;
import static com.increff.pos.util.FormUtil.validateForm;
import static com.increff.pos.util.MapperUtil.mapper;

@Service
public class ReportDto {
    @Autowired
    private OrderService orderService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private ProductService productService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private DailyReportService dailyReportService;

    public List<DailyReportData> getDailyReport(String startDate, String endDate) throws ApiException {

        ZonedDateTime startTime = ZonedDateTime.parse(startDate);
        ZonedDateTime endTime = ZonedDateTime.parse(endDate);
        System.out.println(startTime);


        List<DailyReport> dailyReports = dailyReportService.getByDateRange(startTime, endTime);
        List<DailyReportData> dataList = new ArrayList<>();
        for (DailyReport report : dailyReports) {
            DailyReportData data = mapper(report, DailyReportData.class);
            data.setDate(report.getDate().toLocalDate().toString());
            dataList.add(data);
        }
        return dataList;
    }

    public List<BrandForm> getBrandReport() throws ApiException {
        return mapper(brandService.getAll(), BrandForm.class);
    }

    public List<InventoryReportData> getInventoryReport() throws ApiException {
        List<Brand> brandList = brandService.getAll();
        List<Product> products = productService.getAll();
        Map<Long, Long> reportMap = new HashMap<>();
        for (Brand brand : brandList) {
            reportMap.put(brand.getId(), 0L);
        }
        for (Product product : products) {
            Inventory inventory = inventoryService.getById(product.getId());
            reportMap.put(product.getBrandId(), reportMap.get(product.getBrandId()) + inventory.getQuantity());
        }
        List<InventoryReportData> report = new ArrayList<>();
        for (Brand brand : brandList) {
            InventoryReportData data = mapper(brand, InventoryReportData.class);
            data.setQuantity(reportMap.get(brand.getId()));
            report.add(data);
        }
        return report;
    }


    public List<ReportData> getSalesReport(ReportForm form) throws ApiException {
        validateForm(form);
        normalizeForm(form);
        ZonedDateTime startDate = ZonedDateTime.parse(form.getStartDate());
        ZonedDateTime endDate = ZonedDateTime.parse(form.getEndDate());
        Map<Long, Brand> brandMap = getBrandMap(form);
        List<Order> orders = orderService.getByDatetimeRange(startDate, endDate);
        Map<Long, ReportData> reportMap = new HashMap<>();
        for (Order order : orders) {
            List<OrderItem> orderItems = orderItemService.getListByParameter("orderId", order.getId());
            for (OrderItem orderItem : orderItems) {
                Product product = productService.getById(orderItem.getProductId());
                if (brandMap.get(product.getBrandId()) != null) {
                    ReportData data = reportMap.get(product.getBrandId());
                    if (data == null) {
                        ReportData newdata = new ReportData();
                        newdata.setBrand(brandMap.get(product.getBrandId()).getName());
                        newdata.setCategory(brandMap.get(product.getBrandId()).getCategory());
                        newdata.setQuantity(0L);
                        newdata.setRevenue(0.0);
                        reportMap.put(product.getBrandId(), newdata);
                        data = reportMap.get(product.getBrandId());
                    }
                    data.setQuantity(data.getQuantity() + orderItem.getQuantity());
                    data.setRevenue((data.getRevenue() + orderItem.getQuantity() * orderItem.getSellingPrice()));

                }
            }
        }

        return new ArrayList<>(reportMap.values());
    }

    private Map<Long, Brand> getBrandMap(ReportForm form) {
        Boolean isBrandPresent = form.getBrand() != null && !form.getBrand().isEmpty();
        Boolean isCategoryPresent = form.getCategory() != null && !form.getCategory().isEmpty();
        List<Brand> brands = new ArrayList<>();
        if (isBrandPresent && isCategoryPresent) {
            Brand brand = brandService.getByNameAndCategory(form.getBrand(), form.getCategory());
            if (brand != null) brands.add(brand);
        } else if (isBrandPresent) {
            brands.addAll(brandService.getListByParameter("name", form.getBrand()));
        } else if (isCategoryPresent) {
            brands.addAll(brandService.getListByParameter("category", form.getCategory()));
        } else {
            brands.addAll(brandService.getAll());
        }
        Map<Long, Brand> brandMap = new HashMap<>();
        for (Brand brand : brands) {
            brandMap.put(brand.getId(), brand);
        }
        return brandMap;
    }
}

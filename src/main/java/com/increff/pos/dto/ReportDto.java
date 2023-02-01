package com.increff.pos.dto;

import com.increff.pos.model.ApiException;
import com.increff.pos.model.ReportData;
import com.increff.pos.model.ReportForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.OrderItemService;
import com.increff.pos.service.OrderService;
import com.increff.pos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.increff.pos.util.FormUtil.normalizeForm;
import static com.increff.pos.util.FormUtil.validateForm;

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

    public List<ReportData> getReport(ReportForm form) throws ApiException {
        validateForm(form);
        normalizeForm(form);
        ZonedDateTime startDate = ZonedDateTime.parse(form.getStartDate());
        ZonedDateTime endDate = ZonedDateTime.parse(form.getEndDate());
        Map<Long, BrandPojo> brandMap = getBrandMap(form);
        List<OrderPojo> orders = orderService.getByDatetimeRange(startDate, endDate);
        Map<Long, ReportData> reportMap = new HashMap<>();
        for (OrderPojo order : orders) {
            List<OrderItemPojo> orderItems = orderItemService.getListByParameter("orderId", order.getId());
            for (OrderItemPojo orderItem : orderItems) {
                ProductPojo product = productService.getById(orderItem.getProductId());
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

    private Map<Long, BrandPojo> getBrandMap(ReportForm form) {
        Boolean isBrandPresent = form.getBrand() != null && !form.getBrand().isEmpty();
        Boolean isCategoryPresent = form.getCategory() != null && !form.getCategory().isEmpty();
        List<BrandPojo> brands = new ArrayList<>();
        if (isBrandPresent && isCategoryPresent) {
            BrandPojo brand = brandService.getByNameAndCategory(form.getBrand(), form.getCategory());
            if (brand != null) brands.add(brand);
        } else if (isBrandPresent) {
            brands.addAll(brandService.getListByParameter("name", form.getBrand()));
        } else if (isCategoryPresent) {
            brands.addAll(brandService.getListByParameter("category", form.getCategory()));
        } else {
            brands.addAll(brandService.getAll());
        }
        Map<Long, BrandPojo> brandMap = new HashMap<>();
        for (BrandPojo brand : brands) {
            brandMap.put(brand.getId(), brand);
        }
        return brandMap;
    }
}

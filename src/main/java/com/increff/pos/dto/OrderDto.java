package com.increff.pos.dto;

import com.increff.pos.model.ApiException;
import com.increff.pos.model.OrderData;
import com.increff.pos.model.OrderItemData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.OrderItemService;
import com.increff.pos.service.OrderService;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.MapperUtil;
import com.increff.pos.util.PdfUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.nio.file.Files;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class OrderDto {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private ProductService productService;

    @Transactional(rollbackFor = ApiException.class)
    public String create(List<OrderItemForm> items) throws ApiException {
        OrderPojo orderPojo = orderService.create();
        for (OrderItemForm item : items) {
            ProductPojo productPojo = productService.getOneByParameter("barcode", item.getBarcode());
            if (productPojo == null) {
                throw new ApiException("Invalid Barcode");
            }
            inventoryService.removeQuantity(productPojo.getId(), item.getQuantity());
            OrderItemPojo orderItemPojo = new OrderItemPojo();
            orderItemPojo.setQuantity(item.getQuantity());
            orderItemPojo.setOrderId(orderPojo.getId());
            orderItemPojo.setSellingPrice(item.getSellingPrice());
            orderItemPojo.setProductId(productPojo.getId());
            orderItemService.create(orderItemPojo);
        }
        PdfUtil.convertToPDF(getById(orderPojo.getId()));
        return getInvoiceAsBase64(orderPojo.getId());
    }

    public List<OrderData> getAll() throws ApiException {
        List<OrderPojo> orders = orderService.getAll();
        List<OrderData> orderDataList = new ArrayList<>();
        for (OrderPojo order : orders) {
            OrderData orderData = new OrderData();
            orderData.setId(order.getId());
            ZonedDateTime dateTime = order.getDatetime().truncatedTo(ChronoUnit.SECONDS);
            orderData.setDate(dateTime.toLocalDate().toString());
            orderData.setTime(dateTime.toLocalTime().toString());
            orderDataList.add(orderData);
        }
        return orderDataList;
    }

    public OrderData getById(Long id) throws ApiException {
        OrderPojo order = orderService.getById(id);
        OrderData orderData = new OrderData();
        orderData.setId(order.getId());
        ZonedDateTime dateTime = order.getDatetime().truncatedTo(ChronoUnit.SECONDS);
        orderData.setDate(dateTime.toLocalDate().toString());
        orderData.setTime(dateTime.toLocalTime().toString());
        orderData.setItems(getOrderItems(order.getId()));
        return orderData;
    }

    public List<OrderItemData> getOrderItems(Long id) throws ApiException {
        List<OrderItemPojo> orderItems = orderItemService.getListByParameter("orderId", id);
        List<OrderItemData> items = new ArrayList<>();
        for (OrderItemPojo orderItem : orderItems) {
            ProductPojo product = productService.getById(orderItem.getProductId());
            OrderItemData item = MapperUtil.mapper(orderItem, OrderItemData.class);
            item.setName(product.getName());
            item.setBarcode(product.getBarcode());
            item.setMrp(product.getMrp());
            items.add(item);
        }
        return items;
    }

    public String getInvoiceAsBase64(Long id) throws ApiException {
        try {
            String baseurl = "src/main/resources/invoices/";
            File file = new File(baseurl + "invoice-" + id.toString() + ".pdf");
            byte[] bytes = Files.readAllBytes(file.toPath());

            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            throw new ApiException(e.getMessage());
        }
    }

}

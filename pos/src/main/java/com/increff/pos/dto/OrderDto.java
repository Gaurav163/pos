package com.increff.pos.dto;

import com.increff.pos.model.ApiException;
import com.increff.pos.model.OrderData;
import com.increff.pos.model.OrderItemData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.pojo.Brand;
import com.increff.pos.pojo.Order;
import com.increff.pos.pojo.OrderItem;
import com.increff.pos.pojo.Product;
import com.increff.pos.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static com.increff.pos.util.MapperUtil.mapper;

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
    @Autowired
    private BrandService brandService;
    @Value("${invoicesPath}")
    private String basePath;
    @Value("${baseUrl}")
    private String baseUrl;

    @Transactional(rollbackFor = ApiException.class)
    public OrderData create(List<OrderItemForm> itemForms) throws ApiException {
        Order order = orderService.create();
        for (OrderItemForm itemForm : itemForms) {
            Product product = productService.getOneByParameter("barcode", itemForm.getBarcode());
            if (product == null) {
                throw new ApiException("Invalid barcode");
            }
            inventoryService.reduceInventory(product.getId(), itemForm.getQuantity());
            OrderItem orderItem = new OrderItem();
            orderItem.setQuantity(itemForm.getQuantity());
            orderItem.setOrderId(order.getId());
            orderItem.setSellingPrice(itemForm.getSellingPrice());
            orderItem.setProductId(product.getId());
            orderItemService.create(orderItem);
        }

        return getOrderData(order);
    }

    public List<OrderData> getAll() throws ApiException {

        List<Order> orders = orderService.getAll();
        List<OrderData> orderDataList = new ArrayList<>();
        for (Order order : orders) {
            orderDataList.add(getOrderData(order));
        }
        return orderDataList;

    }

    public OrderData getById(Long id) throws ApiException {
        return getOrderData(orderService.getById(id));
    }

    @Transactional(rollbackFor = Exception.class)
    public OrderData generateInvoice(Long id) throws ApiException {
        Order order = orderService.getById(id);
        if (order == null) {
            throw new ApiException("Order does not exist");
        }
        orderService.createInvoice(id);
        OrderData orderData = getOrderData(order);
        try {
            // calling pdf api and get invoice pdf in base64 format
            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<OrderData> request = new HttpEntity<>(orderData);
            String invoice = restTemplate.postForObject(baseUrl, request, String.class);

            // saving base64 invoice as pdf file
            File file = new File(basePath + "invoice-" + orderData.getId() + ".pdf");
            FileOutputStream fop = new FileOutputStream(file);
            byte[] decodedBytes = Base64.getMimeDecoder().decode(invoice);
            fop.write(decodedBytes);
            fop.flush();
            fop.close();
            return orderData;

        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException("Invoice generation failed");
        }
    }

    public String getInvoiceAsBase64(Long id) throws ApiException {
        try {
            File file = new File(basePath + "invoice-" + id + ".pdf");
            byte[] bytes = Files.readAllBytes(file.toPath());
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            throw new ApiException("Invoice not found");
        }
    }

    private OrderData getOrderData(Order order) throws ApiException {
        if (order == null) return null;
        OrderData orderData = mapper(order, OrderData.class);
        ZonedDateTime dateTime = order.getDatetime().truncatedTo(ChronoUnit.SECONDS);
        orderData.setDate(dateTime.toLocalDate().toString());
        orderData.setTime(dateTime.toLocalTime().toString());
        orderData.setItems(getOrderItems(order.getId()));
        return orderData;
    }

    private List<OrderItemData> getOrderItems(Long id) throws ApiException {
        List<OrderItem> orderItems = orderItemService.getListByParameter("orderId", id);
        List<OrderItemData> items = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            Product product = productService.getById(orderItem.getProductId());
            Brand brand = brandService.getById(product.getBrandId());
            OrderItemData item = mapper(orderItem, OrderItemData.class);
            item.setName(product.getName());
            item.setBarcode(product.getBarcode());
            item.setMrp(product.getMrp());
            item.setBrand(brand.getName());
            item.setCategory(brand.getCategory());
            items.add(item);
        }
        return items;
    }

}

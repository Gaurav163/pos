package com.increff.pos.dto;

import com.increff.pos.model.*;
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

import static com.increff.pos.util.FormUtil.normalizeForm;
import static com.increff.pos.util.FormUtil.validateForm;
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
    public OrderData create(OrderForm orderForm) throws ApiException {
        if (orderForm.getItems().isEmpty()) {
            throw new ApiException("Zero products in order");
        }
        validateForm(orderForm);
        Order order = orderService.create();
        List<String> errors = new ArrayList<>();

        for (OrderItemForm orderItemForm : orderForm.getItems()) {
            try {
                orderItemService.create(getOrderItem(orderItemForm, order));
            } catch (Exception e) {
                errors.add("Barcode: " + orderItemForm.getBarcode() + ", " + e.getMessage());
            }
        }
        if (!errors.isEmpty()) {
            throw new ApiException(String.join("\n", errors));
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

    private OrderItem getOrderItem(OrderItemForm orderItemForm, Order order) throws ApiException {
        normalizeForm(orderItemForm);
        Product product = productService.getOneByParameter("barcode", orderItemForm.getBarcode());
        if (product == null) {
            throw new ApiException("Invalid barcode");
        }
        inventoryService.reduceInventory(product.getId(), orderItemForm.getQuantity());
        OrderItem orderItem = mapper(orderItemForm, OrderItem.class);
        orderItem.setOrderId(order.getId());
        orderItem.setProductId(product.getId());
        return orderItem;
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

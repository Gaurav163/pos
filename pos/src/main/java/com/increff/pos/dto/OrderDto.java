package com.increff.pos.dto;

import com.increff.pos.model.*;
import com.increff.pos.pojo.*;
import com.increff.pos.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import static com.increff.pos.util.FormUtil.normalizeList;
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

    public List<OrderData> getBySizeAndPage(Long size, Long lastId) throws ApiException {
        List<Order> orders = orderService.getBySizeAndPage(size, lastId);
        List<OrderData> orderDataList = new ArrayList<>();
        for (Order order : orders) {
            orderDataList.add(getOrderData(order));
        }
        return orderDataList;

    }

    public OrderData getById(Long id) throws ApiException {
        return getOrderData(orderService.getById(id));
    }

    @Transactional(rollbackFor = ApiException.class)
    public String getInvoice(Long id) throws ApiException {
        Order order = orderService.getById(id);
        if (order == null) {
            throw new ApiException("Order not found in system");
        }
        if (!order.getInvoiced()) {
            OrderData orderData = getOrderData(order);
            String invoice = generateInvoice(orderData);

            saveInvoice(orderData, invoice);
            orderService.createInvoice(order.getId());
        }
        return getInvoiceAsBase64(id);
    }

    @Transactional(rollbackFor = ApiException.class)
    public OrderData create(OrderForm orderForm) throws ApiException {
        if (orderForm.getItems().isEmpty()) {
            throw new ApiException("Zero products in order");
        }
        Order order = orderService.create();
        List<String> validationErrors = validateOrderItemList(orderForm.getItems());
        if (!validationErrors.isEmpty()) {
            throw new ApiException(String.join("\n", validationErrors));
        }

        String errors = createOrderItems(order, orderForm.getItems());
        if (!errors.isEmpty()) {
            throw new ApiException(String.join("\n", errors));
        }
        return getOrderData(order);
    }

    private String createOrderItems(Order order, List<OrderItemForm> orderItemForms) throws ApiException {
        normalizeList(orderItemForms);
        List<String> barcodes = new ArrayList<>();
        for (OrderItemForm orderItemForm : orderItemForms) {
            barcodes.add(orderItemForm.getBarcode());
        }
        Map<String, Product> barcodeProductMap = productService.getListByBarcodeList(barcodes);

        String errors1 = validateBarcodes(orderItemForms, barcodeProductMap);
        if (errors1 != null) return errors1;

        String errors2 = validateInventory(orderItemForms, barcodeProductMap);
        if (errors2 != null) return errors2;

        for (OrderItemForm orderItemForm : orderItemForms) {
            Product product = barcodeProductMap.get(orderItemForm.getBarcode());
            inventoryService.reduceInventory(product.getId(), orderItemForm.getQuantity());
            OrderItem orderItem = mapper(orderItemForm, OrderItem.class);
            orderItem.setOrderId(order.getId());
            orderItem.setProductId(product.getId());
            orderItemService.create(orderItem);
        }
        return "";
    }

    private String validateInventory(List<OrderItemForm> orderItemForms, Map<String, Product> barcodeProductMap) {
        List<String> errors = new ArrayList<>();
        for (OrderItemForm orderItemForm : orderItemForms) {
            Product product = barcodeProductMap.get(orderItemForm.getBarcode());
            Inventory inventory = inventoryService.getById(product.getId());
            if (inventory.getQuantity() < orderItemForm.getQuantity()) {
                errors.add("For barcode:\"" + orderItemForm.getBarcode() + "\" only " + inventory.getQuantity() + " quantity left");
            }
        }
        if (!errors.isEmpty()) {
            return "Some products have low inventory.\n" + String.join("\n", errors);
        }
        return null;
    }

    private String validateBarcodes(List<OrderItemForm> orderItemForms, Map<String, Product> barcodeProductMap) {
        List<String> errors = new ArrayList<>();
        for (OrderItemForm orderItemForm : orderItemForms) {
            Product product = barcodeProductMap.get(orderItemForm.getBarcode());
            if (product == null) {
                errors.add(orderItemForm.getBarcode());
            }
        }
        if (!errors.isEmpty()) {
            return "There are some invalid barcode.\n Invalid barcodes list : " + String.join(", ", errors);
        }
        return null;
    }

    private List<String> validateOrderItemList(List<OrderItemForm> orderItemForms) {
        List<String> errors = new ArrayList<>();
        for (OrderItemForm orderItemForm : orderItemForms) {
            try {
                validateForm(orderItemForm);
            } catch (ApiException e) {
                errors.add("Barcode - " + orderItemForm.getBarcode() + " : " + e.getMessage());
            }
        }
        return errors;
    }

    private void saveInvoice(OrderData orderData, String invoice) throws ApiException {
        try {
            File file = new File(basePath + "invoice-" + orderData.getId() + ".pdf");
            FileOutputStream fop = new FileOutputStream(file);
            byte[] decodedBytes = Base64.getMimeDecoder().decode(invoice);
            fop.write(decodedBytes);
            fop.flush();
            fop.close();
        } catch (IOException e) {
            throw new ApiException("Something went wrong with invoice file, Contact admin");
        }
    }

    private String generateInvoice(OrderData orderData) throws ApiException {
        try {
            // calling pdf api and get invoice pdf in base64 format
            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<OrderData> request = new HttpEntity<>(orderData);
            return restTemplate.postForObject(baseUrl, request, String.class);
        } catch (ResourceAccessException e) {
            throw new ApiException("Unable to connect with invoice app");
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode().is4xxClientError()) {
                throw new ApiException("Error while requesting invoice app : " + e.getResponseBodyAsString());
            } else {
                throw new ApiException("Server error occurs while creating invoice");
            }
        }
    }

    protected String getInvoiceAsBase64(Long id) throws ApiException {
        try {
            File file = new File(basePath + "invoice-" + id + ".pdf");
            byte[] bytes = Files.readAllBytes(file.toPath());
            return Base64.getEncoder().encodeToString(bytes);
        } catch (IOException e) {
            throw new ApiException("Something went wrong with invoice file, Contact admin");
        }
    }

    private OrderData getOrderData(Order order) throws ApiException {
        if (order == null) return null;
        OrderData orderData = mapper(order, OrderData.class);
        ZonedDateTime dateTime = order.getDatetime().truncatedTo(ChronoUnit.SECONDS);
        orderData.setDatetime(dateTime.toOffsetDateTime().toString());
        orderData.setItems(getOrderItems(order.getId()));
        return orderData;
    }

    private List<OrderItemData> getOrderItems(Long id) throws ApiException {
        List<OrderItem> orderItems = orderItemService.getListByParameter("orderId", id);
        // get product map
        List<Long> productIdList = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            productIdList.add(orderItem.getProductId());
        }
        Map<Long, Product> productMap = productService.getListByIdList(productIdList);
        // get brand map
        List<Long> brandIdList = new ArrayList<>();
        for (Product product : productMap.values()) {
            brandIdList.add(product.getBrandId());
        }
        Map<Long, Brand> brandMap = brandService.getListByIdList(brandIdList);
        // create OrderItemData list
        List<OrderItemData> items = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            Product product = productMap.get(orderItem.getProductId());
            Brand brand = brandMap.get(product.getBrandId());
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

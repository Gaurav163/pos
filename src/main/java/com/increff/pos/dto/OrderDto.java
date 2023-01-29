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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
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
    public void create(List<OrderItemForm> items) throws ApiException {
        OrderPojo orderPojo = orderService.create();
        for (OrderItemForm item : items) {
            ProductPojo productPojo = productService.getOneByParameter("barcode", item.getBarcode());
            inventoryService.removeQuantity(productPojo.getId(), item.getQuantity());
            OrderItemPojo orderItemPojo = new OrderItemPojo();
            orderItemPojo.setQuantity(item.getQuantity());
            orderItemPojo.setOrderId(orderPojo.getId());
            orderItemPojo.setSellingPrice(item.getSellingPrice());
            orderItemPojo.setProductId(productPojo.getId());
            orderItemService.create(orderItemPojo);
        }
    }

    public List<OrderData> getAll() throws ApiException {
        return MapperUtil.mapper(orderService.getAll(), OrderData.class);
    }

    public OrderData getById(Long id) throws ApiException {
        OrderData order = MapperUtil.mapper(orderService.getById(id), OrderData.class);
        List<OrderItemPojo> itemPojos = orderItemService.getListByParameter("orderId", id);
        List<OrderItemData> items = new ArrayList<>();
        for (OrderItemPojo itemPojo : itemPojos) {
            ProductPojo product = productService.getById(itemPojo.getProductId());
            OrderItemData item = MapperUtil.mapper(itemPojo, OrderItemData.class);
            item.setName(product.getName());
            item.setBarcode(product.getBarcode());
            item.setMrp(product.getMrp());
            items.add(item);
        }
        order.setItems(items);
        return order;
    }
}

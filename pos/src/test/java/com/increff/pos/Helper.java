package com.increff.pos;

import com.increff.pos.dao.*;
import com.increff.pos.model.BrandForm;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.model.ProductForm;
import com.increff.pos.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
public class Helper {
    @Autowired
    private BrandDao brandDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private InventoryDao inventoryDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderItemDao orderItemDao;

    public Brand getBrand(String name, String category) {
        Brand brand = new Brand();
        brand.setName(name);
        brand.setCategory(category);
        return brand;
    }

    public BrandForm getBrandForm(String name, String category) {
        BrandForm brandForm = new BrandForm();
        brandForm.setName(name);
        brandForm.setCategory(category);
        return brandForm;
    }

    public Brand createBrand(String name, String category) {
        return brandDao.create(getBrand(name, category));
    }

    public void generateBrands(int count) {
        for (int i = 1; i <= count; i++) {
            createBrand("brand" + i, "category" + i);
        }
    }


    public Product getProduct(String name, String barcode, Long brandId, Double mrp) {
        Product product = new Product();
        product.setName(name);
        product.setBarcode(barcode);
        product.setBrandId(brandId);
        product.setMrp(mrp);
        return product;
    }

    public Product createProduct(String name, String barcode, Long brandId, Double mrp) {
        return productDao.create(getProduct(name, barcode, brandId, mrp));
    }

    public ProductForm createProductForm(String name, String barcode, String brand, String category, Double mrp) {
        Brand brandPojo = createBrand(brand, category);
        ProductForm form = new ProductForm();
        form.setName(name);
        form.setBarcode(barcode);
        form.setBrand(brand);
        form.setCategory(category);
        form.setMrp(mrp);
        return form;
    }

    public void generateProducts(int count) {
        for (int i = 1; i <= count; i++) {
            createProduct("name" + i, "barcode" + i, (long) i, i * 2.3);
        }
    }

    public Inventory getInventory(Long productId, Long quantity) {
        Inventory inventory = new Inventory();
        inventory.setId(productId);
        inventory.setQuantity(quantity);
        return inventory;
    }

    public Inventory createInventory(Long productId, Long quantity) {
        return inventoryDao.create(getInventory(productId, quantity));
    }

    public InventoryForm createInventoryForm(String barcode, Long quantity) {
        Brand brand = createBrand("brand1", "category1");
        Product product = createProduct("product1", barcode, brand.getId(), 90.99);
        InventoryForm form = new InventoryForm();
        form.setQuantity(quantity);
        form.setBarcode(barcode);
        return form;
    }

    public Order getOrder(ZonedDateTime datetime) {
        Order order = new Order();
        order.setDatetime(datetime);
        order.setInvoiced(false);
        return order;
    }

    public Order createOrder(ZonedDateTime datetime) {
        return orderDao.create(getOrder(datetime));
    }

    public Order createOrder() {
        return orderDao.create(getOrder(ZonedDateTime.now()));
    }

    public OrderItem getOrderItem(Long quantity, Double sellingPrice) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(99L);
        orderItem.setProductId(99L);
        orderItem.setQuantity(quantity);
        orderItem.setSellingPrice(sellingPrice);
        return orderItem;
    }

    public OrderItem createOrderItem(Long quantity, Double sellingPrice) {
        return orderItemDao.create(getOrderItem(quantity, sellingPrice));
    }


}

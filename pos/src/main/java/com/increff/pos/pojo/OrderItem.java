package com.increff.pos.pojo;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(indexes = {
        @Index(name = "idx_order_items_orderID", columnList = "orderId"),
        @Index(name = "idx_order_items_productID", columnList = "productId")
})
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long orderId;
    @Column(nullable = false)
    private Long productId;
    @Column(nullable = false)
    private Long quantity;
    @Column(nullable = false)
    private Double sellingPrice;
}

package com.increff.pos.pojo;

import lombok.Data;

import javax.persistence.*;

import static com.increff.pos.pojo.PojoConstants.ORDER_ITEM_SEQUENCE;
import static com.increff.pos.pojo.PojoConstants.ORDER_ITEM_TABLE_NAME;

@Data
@Entity
@Table(name = ORDER_ITEM_TABLE_NAME, indexes = {
        @Index(name = "idx_order_items_orderID", columnList = "orderId"),
        @Index(name = "idx_order_items_productID", columnList = "productId")
})
public class OrderItemPojo {
    @SequenceGenerator(allocationSize = 1,
            name = ORDER_ITEM_SEQUENCE,
            sequenceName = ORDER_ITEM_SEQUENCE,
            initialValue = 10001)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = ORDER_ITEM_SEQUENCE)
    @Id
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

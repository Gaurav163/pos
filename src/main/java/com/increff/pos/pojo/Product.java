package com.increff.pos.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static com.increff.pos.pojo.PojoConstants.PRODUCT_SEQUENCE;
import static com.increff.pos.pojo.PojoConstants.PRODUCT_TABLE_NAME;

@Data
@NoArgsConstructor
@Entity
@Table(name = PRODUCT_TABLE_NAME, indexes = {
        @Index(name = "idx_products_barcode", columnList = "barcode")
})
public class Product {
    @Id
    @SequenceGenerator(allocationSize = 1,
            name = PRODUCT_SEQUENCE,
            sequenceName = PRODUCT_SEQUENCE)
    @GeneratedValue(strategy = GenerationType.AUTO,
            generator = PRODUCT_SEQUENCE)
    private Long id;
    @Column(nullable = false, unique = true)
    private String barcode;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Double mrp;
    @Column(nullable = false)
    private Long brandId;
}

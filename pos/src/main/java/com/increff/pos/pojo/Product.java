package com.increff.pos.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(indexes = {
        @Index(name = "idx_products_barcode", columnList = "barcode")
})
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

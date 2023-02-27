package com.increff.pos.pojo;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
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

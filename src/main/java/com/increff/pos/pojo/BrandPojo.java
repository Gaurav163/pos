package com.increff.pos.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static com.increff.pos.pojo.PojoConstants.BRAND_SEQUENCE;
import static com.increff.pos.pojo.PojoConstants.BRAND_TABLE_NAME;

@Entity
@Data
@Table(name = BRAND_TABLE_NAME)
@NoArgsConstructor
public class BrandPojo {

    @Id
    @SequenceGenerator(allocationSize = 1,
            name = BRAND_SEQUENCE,
            sequenceName = BRAND_SEQUENCE)
    @GeneratedValue(strategy = GenerationType.AUTO,
            generator = BRAND_SEQUENCE)
    Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String category;

    public BrandPojo(String name, String category) {
        this.name = name;
        this.category = category;
    }
}

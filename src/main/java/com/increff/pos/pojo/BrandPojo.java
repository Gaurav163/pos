package com.increff.pos.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static com.increff.pos.pojo.PojoConstants.BRAND_SEQUENCE;
import static com.increff.pos.pojo.PojoConstants.BRAND_TABLE_NAME;

@Entity
@Data
@Table(name = BRAND_TABLE_NAME, uniqueConstraints = {
        @UniqueConstraint(name = "uq_brands_name_category", columnNames = {"name", "category"})
})
@NoArgsConstructor
public class BrandPojo {

    @Id
    @SequenceGenerator(allocationSize = 1,
            name = BRAND_SEQUENCE,
            sequenceName = BRAND_SEQUENCE)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = BRAND_SEQUENCE)
    Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String category;

}

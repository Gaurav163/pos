package com.increff.pos.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "uq_brands_name_category", columnNames = {"name", "category"})
})
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String category;

}

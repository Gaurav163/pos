package com.increff.pos.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

import static com.increff.pos.pojo.PojoConstants.ORDER_SEQUENCE;

@Data
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class OrderPojo {

    @Id
    @SequenceGenerator(allocationSize = 1,
            name = ORDER_SEQUENCE,
            sequenceName = ORDER_SEQUENCE,
            initialValue = 10001)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = ORDER_SEQUENCE)
    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date datetime;
}

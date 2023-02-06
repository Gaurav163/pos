package com.increff.pos.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.ZonedDateTime;

import static com.increff.pos.pojo.PojoConstants.ORDER_SEQUENCE;
import static com.increff.pos.pojo.PojoConstants.ORDER_TABLE_NAME;

@Data
@NoArgsConstructor
@Entity
@Table(name = ORDER_TABLE_NAME)
public class Order {

    @Id
    @SequenceGenerator(allocationSize = 1,
            name = ORDER_SEQUENCE,
            sequenceName = ORDER_SEQUENCE,
            initialValue = 10001)
    @GeneratedValue(strategy = GenerationType.AUTO,
            generator = ORDER_SEQUENCE)
    private Long id;
    @Column(nullable = false)
    private ZonedDateTime datetime;
    @Column(nullable = false)
    private Boolean invoiced;
}

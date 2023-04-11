package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.ZonedDateTime;

@Getter
@Setter
@ToString
@Entity
public class DailyReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long invoicedOrdersCount;
    private Long invoicedItemsCount;
    private Double totalRevenue;
    private ZonedDateTime date;
    private Long lastOrderId;

}

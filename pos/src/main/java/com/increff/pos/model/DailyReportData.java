package com.increff.pos.model;

import lombok.Data;

@Data
public class DailyReportData {
    private Long id;
    private Long invoicedOrdersCount;
    private Long invoicedItemsCount;
    private Double totalRevenue;
    private String date;
}

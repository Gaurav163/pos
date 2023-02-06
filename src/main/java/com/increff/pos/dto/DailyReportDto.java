package com.increff.pos.dto;

import com.increff.pos.pojo.DailyReport;
import com.increff.pos.pojo.OrderItem;
import com.increff.pos.pojo.Order;
import com.increff.pos.service.DailyReportService;
import com.increff.pos.service.OrderItemService;
import com.increff.pos.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class DailyReportDto {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private DailyReportService dailyReportService;

    @Scheduled(cron = "0 27 18 * * *", zone = "Asia/Kolkata")
    public void scheduleFixedDelayTask() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        ZonedDateTime lastday = now.minusDays(1);
        List<Order> orders = orderService.getByDatetimeRange(lastday, now);

        Double totalRevenue = 0.0;
        Long ordersCount = (long) orders.size();
        Long itemsCount = 0L;
        for (Order order : orders) {
            List<OrderItem> items = orderItemService.getListByParameter("orderId", order.getId());
            for (OrderItem item : items) {
                itemsCount += item.getQuantity();
                totalRevenue += item.getQuantity() * item.getSellingPrice();
            }
        }

        DailyReport dailyReport = new DailyReport();
        dailyReport.setDate(now.truncatedTo(ChronoUnit.DAYS));
        dailyReport.setInvoicedItemsCount(itemsCount);
        dailyReport.setInvoicedOrdersCount(ordersCount);
        dailyReport.setTotalRevenue(totalRevenue);
        dailyReportService.create(dailyReport);
        System.out.println(dailyReport);
    }
}

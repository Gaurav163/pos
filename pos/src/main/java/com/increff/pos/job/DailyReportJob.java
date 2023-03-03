package com.increff.pos.job;

import com.increff.pos.pojo.DailyReport;
import com.increff.pos.pojo.Order;
import com.increff.pos.pojo.OrderItem;
import com.increff.pos.service.DailyReportService;
import com.increff.pos.service.OrderItemService;
import com.increff.pos.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class DailyReportJob {
    private final Integer batchSize = 100;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private DailyReportService dailyReportService;

    @Scheduled(cron = "0 0/10 * * * *", zone = "Asia/Kolkata")
    public void run() {
        runJob(ZonedDateTime.now().truncatedTo(ChronoUnit.MINUTES));
    }

    public void runJob(ZonedDateTime currentTime) {

        DailyReport lastReport = dailyReportService.getLastReport();
        Long lastOrderId = lastReport == null ? 1L : lastReport.getLastOrderId() + 1;
        while (true) {
            List<Order> orders = orderService.getNextBatch(batchSize, lastOrderId);
            if (orders.isEmpty()) break;
            lastOrderId = generateReport(orders) + 1;
        }
    }

    public Long generateReport(List<Order> orders) {
        ZonedDateTime currentReportDate = orders.get(0).getDatetime().truncatedTo(ChronoUnit.DAYS);
        Long lastOrderId = null;
        Double totalRevenue = 0.0;
        Long ordersCount = 0L;
        Long itemsCount = 0L;
        for (Order order : orders) {
            List<OrderItem> items = orderItemService.getListByParameter("orderId", order.getId());
            ordersCount += 1L;
            lastOrderId = order.getId();
            for (OrderItem item : items) {
                itemsCount += item.getQuantity();
                totalRevenue += item.getQuantity() * item.getSellingPrice();
            }
            if (!currentReportDate.equals(order.getDatetime().truncatedTo(ChronoUnit.DAYS))) {
                dailyReportService.updateDailyReport(currentReportDate, lastOrderId, ordersCount, itemsCount, totalRevenue);
                currentReportDate = order.getDatetime().truncatedTo(ChronoUnit.DAYS);
                totalRevenue = 0.0;
                ordersCount = 0L;
                itemsCount = 0L;
            }
        }
        dailyReportService.updateDailyReport(currentReportDate, lastOrderId, ordersCount, itemsCount, totalRevenue);
        return lastOrderId;
    }
}

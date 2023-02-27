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
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private DailyReportService dailyReportService;

    @Scheduled(cron = "30 20 11 * * *", zone = "Asia/Kolkata")
    public void dailyScheduler() {
        ZonedDateTime now = ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS);
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
        dailyReport.setDate(now);
        dailyReport.setInvoicedItemsCount(itemsCount);
        dailyReport.setInvoicedOrdersCount(ordersCount);
        dailyReport.setTotalRevenue(totalRevenue);
        dailyReportService.create(dailyReport);
    }
}

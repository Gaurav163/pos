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

    @Scheduled(cron = "0 0/10 * * * *", zone = "Asia/Kolkata")
    public void run() {
        System.out.println(ZonedDateTime.now());
        runJob(ZonedDateTime.now().truncatedTo(ChronoUnit.MINUTES));
    }

    public void runJob(ZonedDateTime currentTime) {
        ZonedDateTime startTime = null;
        DailyReport lastReport = dailyReportService.getLastReport();
        if (lastReport != null) {
            startTime = lastReport.getLastEntryTime();
        } else {
            Order firstOrder = orderService.getFirstOrder();
            if (firstOrder == null) return;
            startTime = firstOrder.getDatetime().minusMinutes(firstOrder.getDatetime().getMinute() % 10).truncatedTo(ChronoUnit.MINUTES);
        }
        while (!currentTime.equals(startTime)) {
            ZonedDateTime endTime = startTime.plusMinutes(10);
            generateReport(startTime, endTime);
            startTime = endTime;
        }
    }

    public void generateReport(ZonedDateTime startTime, ZonedDateTime endTime) {
        List<Order> orders = orderService.getByDatetimeRange(startTime, endTime);

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
        dailyReportService.updateDailyReport(startTime.truncatedTo(ChronoUnit.DAYS), endTime, ordersCount, itemsCount, totalRevenue);
    }
}

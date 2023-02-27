package com.increff.pos.dto;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.TestHelper;
import com.increff.pos.dao.DailyReportDao;
import com.increff.pos.job.DailyReportJob;
import com.increff.pos.pojo.DailyReport;
import com.increff.pos.pojo.Order;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DailyReportDtoTest extends AbstractUnitTest {
    @Autowired
    private TestHelper testHelper;
    @Autowired
    private DailyReportJob dailyReportDto;
    @Autowired
    private DailyReportDao dailyReportDao;

    @Test
    public void testScheduler() {
        ZonedDateTime currentTime = ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS);
        Order order1 = testHelper.createOrder(currentTime.minusDays(2));
        Order order2 = testHelper.createOrder(currentTime.minusHours(23));
        Order order3 = testHelper.createOrder(currentTime);
        testHelper.createOrderItem(order1.getId(), 1L, 10L, 150.0);
        testHelper.createOrderItem(order1.getId(), 2L, 10L, 150.0);
        testHelper.createOrderItem(order1.getId(), 3L, 10L, 150.0);
        testHelper.createOrderItem(order2.getId(), 1L, 10L, 150.0);
        testHelper.createOrderItem(order2.getId(), 2L, 10L, 150.0);
        testHelper.createOrderItem(order2.getId(), 3L, 5L, 150.0);
        testHelper.createOrderItem(order3.getId(), 1L, 10L, 150.0);
        testHelper.createOrderItem(order3.getId(), 2L, 5L, 150.0);
        dailyReportDto.dailyScheduler();
        List<DailyReport> dailyReportList = dailyReportDao.getByDatetimeRange(currentTime.minusDays(1), currentTime);
        assertEquals(1, dailyReportList.size());
        DailyReport report = dailyReportList.get(0);
        assertEquals(Long.valueOf(40), report.getInvoicedItemsCount());
        assertEquals(Long.valueOf(2), report.getInvoicedOrdersCount());
        assertEquals(Double.valueOf(6000), report.getTotalRevenue());

    }
}

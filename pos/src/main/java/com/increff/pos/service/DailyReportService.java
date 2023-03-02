package com.increff.pos.service;

import com.increff.pos.dao.DailyReportDao;
import com.increff.pos.pojo.DailyReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class DailyReportService {
    @Autowired
    private DailyReportDao dailyReportDao;

    public DailyReport create(DailyReport report) {
        dailyReportDao.create(report);
        return report;
    }

    public DailyReport getByDate(ZonedDateTime date) {
        return dailyReportDao.getByParameter("date", date);
    }

    public DailyReport getLastReport() {
        return dailyReportDao.getLastReport();
    }

    public void updateDailyReport(ZonedDateTime date, ZonedDateTime reportEndTime, Long orderCount, Long itemCount, Double totalRevenue) {
        DailyReport report = getByDate(date);
        if (report == null) {
            DailyReport newReport = new DailyReport();
            newReport.setInvoicedItemsCount(itemCount);
            newReport.setInvoicedOrdersCount(orderCount);
            newReport.setTotalRevenue(totalRevenue);
            newReport.setDate(date);
            newReport.setLastEntryTime(reportEndTime);
            create(newReport);
        } else {
            report.setInvoicedItemsCount(report.getInvoicedItemsCount() + itemCount);
            report.setInvoicedOrdersCount(report.getInvoicedOrdersCount() + orderCount);
            report.setTotalRevenue(report.getTotalRevenue() + totalRevenue);
            report.setLastEntryTime(reportEndTime);
        }
    }


    public List<DailyReport> getByDateRange(ZonedDateTime startTime, ZonedDateTime endTime) {
        return dailyReportDao.getByDatetimeRange(startTime, endTime);
    }

}

package com.increff.pos.service;

import com.increff.pos.dao.DailyReportDao;
import com.increff.pos.pojo.DailyReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public List<DailyReport> getAll() {
        return dailyReportDao.getAll();
    }

}

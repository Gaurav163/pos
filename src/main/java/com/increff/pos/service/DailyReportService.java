package com.increff.pos.service;

import com.increff.pos.dao.DailyReportDao;
import com.increff.pos.pojo.DailyReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class DailyReportService {
    @Autowired
    private DailyReportDao dailyReportDao;

    public void create(DailyReport report) {
        dailyReportDao.create(report);
        System.out.println(report);
    }

}

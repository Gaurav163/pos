package com.increff.pos.dao;

import com.increff.pos.pojo.DailyReport;
import org.springframework.stereotype.Repository;

@Repository
public class DailyReportDao extends AbstractDao<DailyReport> {
    protected DailyReportDao() {
        super(DailyReport.class);
    }
}

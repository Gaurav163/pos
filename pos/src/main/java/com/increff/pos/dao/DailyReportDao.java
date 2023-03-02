package com.increff.pos.dao;

import com.increff.pos.pojo.DailyReport;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.time.ZonedDateTime;
import java.util.List;

@Repository
public class DailyReportDao extends AbstractDao<DailyReport> {
    protected DailyReportDao() {
        super(DailyReport.class);
    }

    public List<DailyReport> getByDatetimeRange(ZonedDateTime startTime, ZonedDateTime endTime) {
        String SELECT_BY_DATETIME_RANGE = "select b from DailyReport b where date>=:startTime and date<=:endTime";
        TypedQuery<DailyReport> query = getQuery(SELECT_BY_DATETIME_RANGE);
        query.setParameter("startTime", startTime);
        query.setParameter("endTime", endTime);
        return query.getResultList();
    }

    public DailyReport getLastReport() {
        String queryString = "select b from DailyReport b order by id desc";
        TypedQuery<DailyReport> query = getQuery(queryString);
        query.setMaxResults(1);
        return getSingle(query);
    }
}

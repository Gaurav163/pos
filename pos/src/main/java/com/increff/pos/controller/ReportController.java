package com.increff.pos.controller;

import com.increff.pos.dto.ReportDto;
import com.increff.pos.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportDto reportDto;

    @RequestMapping(path = "/sales", method = RequestMethod.POST)
    public List<ReportData> getSalesReport(@RequestBody ReportForm form) throws ApiException {
        return reportDto.getSalesReport(form);
    }

    @RequestMapping(path = "/brand", method = RequestMethod.GET)
    public List<BrandData> getBrandReport() throws ApiException {
        return reportDto.getBrandReport();
    }

    @RequestMapping(path = "/inventory", method = RequestMethod.GET)
    public List<InventoryReportData> getInventoryReport() throws ApiException {
        return reportDto.getInventoryReport();
    }

    @RequestMapping(value = "/daily", method = RequestMethod.GET)
    public List<DailyReportData> getDailyReport() throws ApiException {
        return reportDto.getDailyReport();
    }


    @RequestMapping(path = "/checkdate", method = RequestMethod.POST)
    public void printDate(@RequestBody BrandForm form) {
        String date = form.getName();
        System.out.println(date);
        DateTimeFormatter fmt = DateTimeFormatter.ISO_DATE_TIME;

        ZonedDateTime time = ZonedDateTime.now();
        ZonedDateTime time1 = ZonedDateTime.parse(date);
        time1 = time1.withZoneSameInstant(ZoneId.of("Asia/Kolkata"));
        System.out.println(time.format(fmt));
        System.out.println(time1.format(fmt));
    }
}

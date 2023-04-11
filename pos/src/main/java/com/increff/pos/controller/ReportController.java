package com.increff.pos.controller;

import com.increff.pos.dto.ReportDto;
import com.increff.pos.model.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportDto reportDto;

    @ApiOperation(value = "Get sales report by applying with filters like start date, end date, brand and category")
    @RequestMapping(path = "/sales", method = RequestMethod.POST)
    public List<ReportData> getSalesReport(@RequestBody ReportForm form) throws ApiException {
        return reportDto.getSalesReport(form);
    }

    @ApiOperation(value = "Get brand report")
    @RequestMapping(path = "/brand", method = RequestMethod.GET)
    public List<BrandForm> getBrandReport() throws ApiException {
        return reportDto.getBrandReport();
    }

    @ApiOperation(value = "Get inventory report")
    @RequestMapping(path = "/inventory", method = RequestMethod.GET)
    public List<InventoryReportData> getInventoryReport() throws ApiException {
        return reportDto.getInventoryReport();
    }

    @ApiOperation(value = "Get daily sales report using filter on date")
    @RequestMapping(value = "/daily", method = RequestMethod.GET)
    public List<DailyReportData> getDailyReport(@RequestParam(value = "startDate") String startDate,
                                                @RequestParam(value = "endDate") String endDate) throws ApiException {
        return reportDto.getDailyReport(startDate, endDate);
    }

}

package com.bishe.web.controller.travel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bishe.common.core.controller.BaseController;
import com.bishe.common.core.domain.AjaxResult;
import com.bishe.system.service.travel.IBizTravelReportService;

@RestController
@RequestMapping("/travel/report")
public class TravelReportController extends BaseController
{
    @Autowired
    private IBizTravelReportService reportService;

    @PreAuthorize("@ss.hasPermi('travel:report:list')")
    @GetMapping("/overview")
    public AjaxResult overview()
    {
        return success(reportService.selectOverview());
    }

    @PreAuthorize("@ss.hasPermi('travel:report:list')")
    @GetMapping("/deptStats")
    public AjaxResult deptStats()
    {
        return success(reportService.selectDeptStats());
    }

    @PreAuthorize("@ss.hasPermi('travel:report:list')")
    @GetMapping("/monthlyStats")
    public AjaxResult monthlyStats()
    {
        return success(reportService.selectMonthlyStats());
    }
}

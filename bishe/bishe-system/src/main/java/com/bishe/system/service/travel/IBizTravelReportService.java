package com.bishe.system.service.travel;

import java.util.List;
import java.util.Map;

public interface IBizTravelReportService
{
    Map<String, Object> selectOverview();

    List<Map<String, Object>> selectDeptStats();

    List<Map<String, Object>> selectMonthlyStats();
}

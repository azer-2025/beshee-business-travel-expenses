package com.bishe.system.mapper.travel;

import java.util.List;
import java.util.Map;

public interface BizTravelReportMapper
{
    Map<String, Object> selectOverview();

    List<Map<String, Object>> selectDeptStats();

    List<Map<String, Object>> selectMonthlyStats();
}

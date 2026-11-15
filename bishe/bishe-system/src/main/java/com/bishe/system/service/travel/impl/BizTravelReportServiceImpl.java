package com.bishe.system.service.travel.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bishe.system.mapper.travel.BizTravelReportMapper;
import com.bishe.system.service.travel.IBizTravelReportService;

@Service
public class BizTravelReportServiceImpl implements IBizTravelReportService
{
    @Autowired
    private BizTravelReportMapper reportMapper;

    @Override
    public Map<String, Object> selectOverview()
    {
        return reportMapper.selectOverview();
    }

    @Override
    public List<Map<String, Object>> selectDeptStats()
    {
        return reportMapper.selectDeptStats();
    }

    @Override
    public List<Map<String, Object>> selectMonthlyStats()
    {
        return reportMapper.selectMonthlyStats();
    }
}

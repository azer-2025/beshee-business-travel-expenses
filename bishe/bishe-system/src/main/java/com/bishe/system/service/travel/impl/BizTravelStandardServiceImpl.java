package com.bishe.system.service.travel.impl;

import java.util.List;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bishe.system.domain.travel.BizTravelStandard;
import com.bishe.system.mapper.travel.BizTravelStandardMapper;
import com.bishe.system.service.travel.IBizTravelStandardService;

@Service
public class BizTravelStandardServiceImpl implements IBizTravelStandardService
{
    @Autowired
    private BizTravelStandardMapper standardMapper;

    @Override
    public BizTravelStandard selectBizTravelStandardById(Long standardId)
    {
        return standardMapper.selectBizTravelStandardById(standardId);
    }

    @Override
    public List<BizTravelStandard> selectBizTravelStandardList(BizTravelStandard standard)
    {
        return standardMapper.selectBizTravelStandardList(standard);
    }

    @Override
    public int insertBizTravelStandard(BizTravelStandard standard)
    {
        return standardMapper.insertBizTravelStandard(standard);
    }

    @Override
    public int updateBizTravelStandard(BizTravelStandard standard)
    {
        return standardMapper.updateBizTravelStandard(standard);
    }

    @Override
    public int deleteBizTravelStandardById(Long standardId)
    {
        return standardMapper.deleteBizTravelStandardById(standardId);
    }

    @Override
    public int deleteBizTravelStandardByIds(Long[] standardIds)
    {
        return standardMapper.deleteBizTravelStandardByIds(standardIds);
    }

    @Override
    public BizTravelStandard selectMatchedStandard(String regionLevel, String jobLevel, Date bizDate)
    {
        return standardMapper.selectMatchedStandard(regionLevel, jobLevel, bizDate);
    }
}

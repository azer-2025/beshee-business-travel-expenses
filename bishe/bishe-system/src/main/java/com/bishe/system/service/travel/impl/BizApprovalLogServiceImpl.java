package com.bishe.system.service.travel.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bishe.common.utils.DateUtils;
import com.bishe.system.domain.travel.BizApprovalLog;
import com.bishe.system.mapper.travel.BizApprovalLogMapper;
import com.bishe.system.service.travel.IBizApprovalLogService;

@Service
public class BizApprovalLogServiceImpl implements IBizApprovalLogService
{
    @Autowired
    private BizApprovalLogMapper approvalLogMapper;

    @Override
    public List<BizApprovalLog> selectBizApprovalLogList(BizApprovalLog log)
    {
        return approvalLogMapper.selectBizApprovalLogList(log);
    }

    @Override
    public int insertBizApprovalLog(BizApprovalLog log)
    {
        if (log.getCreateTime() == null)
        {
            log.setCreateTime(new Date());
        }
        return approvalLogMapper.insertBizApprovalLog(log);
    }
}

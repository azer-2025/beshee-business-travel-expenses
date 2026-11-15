package com.bishe.system.mapper.travel;

import java.util.List;
import com.bishe.system.domain.travel.BizApprovalLog;

public interface BizApprovalLogMapper
{
    List<BizApprovalLog> selectBizApprovalLogList(BizApprovalLog log);

    int insertBizApprovalLog(BizApprovalLog log);
}

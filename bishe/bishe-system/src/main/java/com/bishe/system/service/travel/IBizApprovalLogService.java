package com.bishe.system.service.travel;

import java.util.List;
import com.bishe.system.domain.travel.BizApprovalLog;

public interface IBizApprovalLogService
{
    List<BizApprovalLog> selectBizApprovalLogList(BizApprovalLog log);

    int insertBizApprovalLog(BizApprovalLog log);
}

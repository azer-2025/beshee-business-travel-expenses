package com.bishe.system.service.travel;

import java.util.List;
import com.bishe.system.domain.travel.BizTripApply;

public interface IBizTripApplyService
{
    BizTripApply selectBizTripApplyById(Long applyId);

    List<BizTripApply> selectBizTripApplyList(BizTripApply apply);

    int insertBizTripApply(BizTripApply apply);

    int updateBizTripApply(BizTripApply apply);

    int submitBizTripApply(Long applyId, String username);

    int auditBizTripApply(Long applyId, String action, String comment, Long approverId, String approverName);

    int deleteBizTripApplyById(Long applyId);

    int deleteBizTripApplyByIds(Long[] applyIds);
}

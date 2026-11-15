package com.bishe.system.service.travel;

import java.util.List;
import com.bishe.system.domain.travel.BizReimburse;

public interface IBizReimburseService
{
    BizReimburse selectBizReimburseById(Long reimburseId);

    List<BizReimburse> selectBizReimburseList(BizReimburse reimburse);

    int insertBizReimburse(BizReimburse reimburse);

    int updateBizReimburse(BizReimburse reimburse);

    int submitBizReimburse(Long reimburseId, String username);

    int auditBizReimburse(Long reimburseId, String action, String comment, Long approverId, String approverName);

    int markBizReimbursePaid(Long reimburseId, Long payerId, String payerName, String voucherImage, String payStatus);

    int deleteBizReimburseById(Long reimburseId);

    int deleteBizReimburseByIds(Long[] reimburseIds);
}

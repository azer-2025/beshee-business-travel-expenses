package com.bishe.system.mapper.travel;

import java.util.List;
import com.bishe.system.domain.travel.BizReimburse;

public interface BizReimburseMapper
{
    BizReimburse selectBizReimburseById(Long reimburseId);

    List<BizReimburse> selectBizReimburseList(BizReimburse reimburse);

    int insertBizReimburse(BizReimburse reimburse);

    int updateBizReimburse(BizReimburse reimburse);

    int deleteBizReimburseById(Long reimburseId);

    int deleteBizReimburseByIds(Long[] reimburseIds);
}

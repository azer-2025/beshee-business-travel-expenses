package com.bishe.system.mapper.travel;

import java.util.List;
import com.bishe.system.domain.travel.BizTripApply;

public interface BizTripApplyMapper
{
    BizTripApply selectBizTripApplyById(Long applyId);

    List<BizTripApply> selectBizTripApplyList(BizTripApply apply);

    int insertBizTripApply(BizTripApply apply);

    int updateBizTripApply(BizTripApply apply);

    int deleteBizTripApplyById(Long applyId);

    int deleteBizTripApplyByIds(Long[] applyIds);
}
